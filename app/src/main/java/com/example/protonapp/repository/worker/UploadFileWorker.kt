package com.example.protonapp.repository.worker

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.OpenableColumns
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
    private var getTaskDisposable: Disposable? = null

    override fun doWork(): Result {
        Timber.i("Worker ${this::class.java.simpleName} started.")

        inputData.getString(TASK_ID)?.let { taskId ->
            repository.getTask(taskId)
                    .doOnSuccess { Timber.d("Task to process $it") }
                    .doOnSuccess { repository.startTask(it).subscribe() }
                    .map { sendFile(it) }
                    .subscribe({ }, { Timber.e(it) })
            return ListenableWorker.Result.SUCCESS
        }
        return ListenableWorker.Result.FAILURE
    }

    @Throws(FileNotFoundException::class, IOException::class)
    private fun sendFile(task: Task): Task {
        Timber.d("File Uri: ${task.fileUri}")
        val fileDescriptor = contentResolver.openFileDescriptor(Uri.parse(task.fileUri), READ_MODE)
        fileDescriptor?.let {
            val stream = FileInputStream(fileDescriptor.fileDescriptor)
            val fileName = getFileName(Uri.parse(task.fileUri))
            Timber.i("File to upload: $fileName")
            dropboxClient.files().uploadBuilder(DROP_BOX_DESTINATION + fileName)
                    .withMode(WriteMode.OVERWRITE)
                    .uploadAndFinish(stream, getProgressListener())
            repository.finishTask(task).subscribe()
        }
        return task
    }

    private fun getProgressListener() = IOUtil.ProgressListener { progress ->
        Timber.d("(Progress) Uploaded bytes: $progress")
    }

    private fun getFileName(fileUri: Uri): String {
        if (fileUri.toString().startsWith(CONTENT_PREFIX)) {
            var cursor: Cursor? = null
            try {
                cursor = applicationContext.contentResolver.query(fileUri, null,
                        null, null, null)
                if (cursor != null && cursor.moveToFirst()) {
                    return cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                }
            } finally {
                cursor?.close()
            }
        } else {
            fileUri.lastPathSegment?.let {
                return it.subSequence(it.lastIndexOf("/") + 1, it.length).toString()
            }
        }
        return fileUri.toString()
    }

    companion object {
        const val TASK_ID = "task_id"
        const val DROP_BOX_DESTINATION = "/Proton Files/"
        const val READ_MODE = "r"
        const val CONTENT_PREFIX = "content://"
    }
}
