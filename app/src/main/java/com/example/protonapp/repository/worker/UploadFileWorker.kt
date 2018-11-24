package com.example.protonapp.repository.worker

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import androidx.work.ListenableWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.dropbox.core.util.IOUtil
import com.dropbox.core.v2.DbxClientV2
import com.dropbox.core.v2.files.UploadUploader
import com.dropbox.core.v2.files.WriteMode
import com.example.protonapp.ProtonApplication
import com.example.protonapp.repository.notification.NotificationHelper
import com.example.protonapp.repository.task.Task
import com.example.protonapp.repository.task.TaskRepository
import com.example.protonapp.utils.FileUtils
import com.example.protonapp.utils.WorkManagerUtils.Companion.WORK_REQUEST_TAG
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
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
    private val compositeDisposable = CompositeDisposable()
    private var notificationId: Int? = null
    private var dropBoxUploadUploader: UploadUploader? = null
    private var task: Task? = null
    private var lastProgress = 0L

    override fun doWork(): Result {
        Timber.i("Worker ${this::class.java.simpleName} started.")
        inputData.getString(WORK_REQUEST_TAG)?.let { taskId ->
            repository.getTask(taskId)
                    .doOnSuccess { Timber.d("Task to process $it") }
                    .doOnSuccess { this.task = it }
                    .map { sendFile(it) }
                    .subscribe({ }, { Timber.e(it) })
                    .addTo(compositeDisposable)
            return ListenableWorker.Result.SUCCESS
        }
        return ListenableWorker.Result.FAILURE
    }

    //TODO WorkManager is in alpha11 version so this workaround is needed. Its quite often when
    // `Work interrupted` is observed after `Work Success` and `onStopped()` is called.
    // If upload will be finished properly, then avoid canceling Task and notification
    var isUploadFinishedSuccessfully = false

    override fun onStopped() {
        Timber.d("On Stopped")
        try {
            dropBoxUploadUploader?.abort()
            dropBoxUploadUploader?.close()
        } catch (exception: Exception) {
            Timber.e(exception)
        }
        if (!isUploadFinishedSuccessfully) {
            notificationId?.let { notificationHelper.markNotificationCanceled(it) }
            task?.let {
                repository.cancelTask(it)
                        .subscribe({}, { Timber.e(it) })
                        .addTo(compositeDisposable)
            }
        }
        compositeDisposable.dispose()
    }

    @Throws(FileNotFoundException::class, IOException::class)
    private fun sendFile(task: Task): Task {
        Timber.d("File Uri: ${task.fileUri}")
        val fileDescriptor = contentResolver.openFileDescriptor(Uri.parse(task.fileUri), READ_MODE)
        fileDescriptor?.let {
            val stream = FileInputStream(fileDescriptor.fileDescriptor)
            val fileName = fileUtils.getFileName(Uri.parse(task.fileUri))

            beforeUpload(fileName)
            uploadFile(fileName, stream, it.statSize)
            uploadCompleted(fileName)
        }
        return task
    }

    private fun beforeUpload(fileName: String) {
        Timber.i("File to upload: $fileName")
        task?.let { task ->
            notificationId = notificationHelper.createNotification(task.id, task.name, fileName)
            repository.startTask(task)
                    .subscribe({ this.task = it }, { Timber.e(it) })
                    .addTo(compositeDisposable)
        }
    }

    private fun uploadFile(fileName: String, stream: FileInputStream, fileSize: Long) {
        dropBoxUploadUploader = dropboxClient.files()
                .uploadBuilder(DROP_BOX_DESTINATION + fileName)
                .withMode(WriteMode.OVERWRITE)
                .start()
        dropBoxUploadUploader?.uploadAndFinish(stream, getProgressListener(fileSize))
    }

    private fun uploadCompleted(fileName: String) {
        Timber.i("Upload completed: $fileName")
        notificationId?.let { notificationHelper.markNotificationFinished(it) }
        task?.let { task ->
            repository.finishTask(task)
                    .subscribe({ this.task = it }, { Timber.e(it) })
                    .addTo(compositeDisposable)
        }
        isUploadFinishedSuccessfully = true
    }

    private fun getProgressListener(fileSize: Long) =
            IOUtil.ProgressListener { uploadedBytes ->
                val progress = uploadedBytes * TOTAL_PERCENT / fileSize
                Timber.d("(Progress) Uploaded bytes: $uploadedBytes, percents: $progress")
                if (progress > lastProgress) {
                    lastProgress = progress
                    notificationId?.let { notificationHelper.updateProgress(progress.toInt(), it) }
                }
            }

    companion object {
        const val DROP_BOX_DESTINATION = "/Proton Files/"
        const val READ_MODE = "r"
        const val TOTAL_PERCENT = 100
    }
}
