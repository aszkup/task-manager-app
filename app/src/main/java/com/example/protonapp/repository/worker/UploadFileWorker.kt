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
import com.example.protonapp.repository.task.Task
import com.example.protonapp.repository.task.TaskRepository
import io.reactivex.disposables.Disposable
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance
import timber.log.Timber
import java.io.FileInputStream

class UploadFileWorker(
        context: Context,
        workerParameters: WorkerParameters
) : Worker(context, workerParameters), KodeinAware {

    override val kodein by lazy { (applicationContext as ProtonApplication).kodein }

    private val repository: TaskRepository by instance()
    private val dropboxClient: DbxClientV2 by instance()
    private val contentResolver: ContentResolver by instance()
    private var getTaskDisposable: Disposable? = null

    override fun doWork(): Result {
        Timber.i("Worker ${this::class.java.simpleName} started.")

        inputData.getString(TASK_ID)?.let { taskId ->
            getTaskDisposable = repository.getTask(taskId)
                    .doOnSuccess { Timber.d("Task to process $it") }
                    .subscribe({ sendFile(it) }, { Timber.e(it) })
            return ListenableWorker.Result.SUCCESS
        }
        return ListenableWorker.Result.FAILURE
    }

    private fun sendFile(task: Task) {
        Timber.d("File Uri: ${task.fileUri}")
        val fileDescriptor = contentResolver.openFileDescriptor(Uri.parse(task.fileUri), READ_MODE)
        fileDescriptor?.let {
            val stream = FileInputStream(fileDescriptor.fileDescriptor)
            Uri.parse(task.fileUri).lastPathSegment?.let {
                val fileName = it.subSequence(it.lastIndexOf("/") + 1, it.length)
                Timber.i("File to upload: $fileName")
                dropboxClient.files().uploadBuilder(DROP_BOX_DESTINATION + fileName)
                        .withMode(WriteMode.OVERWRITE)
                        .uploadAndFinish(stream, getProgressListener())
            }
        }
    }

    private fun getProgressListener() = IOUtil.ProgressListener { progress ->
        Timber.d("(Progress) Uploaded bytes: $progress")
    }

    companion object {
        const val TASK_ID = "task_id"
        const val DROP_BOX_DESTINATION = "/Proton Files/"
        const val READ_MODE = "r"
    }
}
