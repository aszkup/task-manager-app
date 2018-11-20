package com.example.protonapp.repository.worker

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import androidx.work.ListenableWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.dropbox.core.util.IOUtil
import com.dropbox.core.v2.DbxClientV2
import com.dropbox.core.v2.files.WriteMode
import com.example.protonapp.ProtonApplication
import com.example.protonapp.repository.notification.NotificationHelper
import com.example.protonapp.repository.task.Task
import com.example.protonapp.repository.task.TaskRepository
import com.example.protonapp.utils.FileUtils
import io.reactivex.disposables.Disposable
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance
import timber.log.Timber
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException


class UploadFileWorker(
        context: Context,
        workerParameters: WorkerParameters
) : Worker(context, workerParameters), KodeinAware {

    override val kodein by lazy { (applicationContext as ProtonApplication).kodein }

    private val repository: TaskRepository by instance()
    private val dropboxClient: DbxClientV2 by instance()
    private val contentResolver: ContentResolver by instance()
    private val fileUtils: FileUtils by instance()
    private val notificationHelper: NotificationHelper by instance()
    private var getTaskDisposable: Disposable? = null
    private var notificationId: Int? = null

    override fun doWork(): Result {
        Timber.i("Worker ${this::class.java.simpleName} started.")
        inputData.getString(TASK_ID)?.let { taskId ->
            repository.getTask(taskId)
                    .doOnSuccess { Timber.d("Task to process $it") }
                    .map { sendFile(it) }
                    .subscribe({ }, { Timber.e(it) })
            return ListenableWorker.Result.SUCCESS
        }
        return ListenableWorker.Result.FAILURE
    }

    @Throws(FileNotFoundException::class, IOException::class)
    private fun sendFile(task: Task): Task {
        var task = task
        Timber.d("File Uri: ${task.fileUri}")
        val fileDescriptor = contentResolver.openFileDescriptor(Uri.parse(task.fileUri), READ_MODE)
        fileDescriptor?.let {
            val stream = FileInputStream(fileDescriptor.fileDescriptor)
            val fileName = fileUtils.getFileName(Uri.parse(task.fileUri))
            Timber.i("File to upload: $fileName")
            repository.startTask(task).subscribe({ task = it }, { Timber.e(it) })
            notificationId = notificationHelper.createNotification(task.id, task.name, fileName)
            dropboxClient.files().uploadBuilder(DROP_BOX_DESTINATION + fileName)
                    .withMode(WriteMode.OVERWRITE)
                    .uploadAndFinish(stream, getProgressListener(it.statSize))
            notificationId?.let { notificationHelper.markNotificationFinished(it) }
            Timber.i("Upload completed: $fileName")
            repository.finishTask(task).subscribe({ task = it }, { Timber.e(it) })
        }
        return task
    }

    private fun getProgressListener(fileSize: Long) =
            IOUtil.ProgressListener { uploadedBytes ->
                val progress = uploadedBytes * 100 / fileSize
                Timber.d("(Progress) Uploaded bytes: $uploadedBytes, percents: $progress")
                notificationId?.let { notificationHelper.updateProgress(progress.toInt(), it) }
            }

    companion object {
        const val TASK_ID = "task_id"
        const val DROP_BOX_DESTINATION = "/Proton Files/"
        const val READ_MODE = "r"
    }
}
