package com.example.protonapp.repository.worker

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.protonapp.ProtonApplication
import com.example.protonapp.repository.task.TaskRepository
import io.reactivex.disposables.Disposable
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance
import timber.log.Timber

class UploadFileWorker(
        context: Context,
        workerParameters: WorkerParameters
) : Worker(context, workerParameters), KodeinAware {

    override val kodein by lazy { (applicationContext as ProtonApplication).kodein }

    private val repository: TaskRepository by instance()
    private var getTaskDisposable: Disposable? = null

    override fun doWork(): Result {
        Timber.i("Worker ${this::class.java.simpleName} started.")

        inputData.getString(TASK_ID)?.let { taskId ->
            getTaskDisposable = repository.getTask(taskId)
                    .subscribe({ Timber.d("Task to process $it") }, { Timber.e(it) })
            return ListenableWorker.Result.SUCCESS
        }
        return ListenableWorker.Result.FAILURE
    }

    companion object {
        const val TASK_ID = "task_id"
    }
}
