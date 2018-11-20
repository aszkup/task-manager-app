package com.example.protonapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.work.WorkManager
import com.example.protonapp.repository.task.TaskRepository
import com.example.protonapp.viewmodel.main.TasksViewModel
import com.example.protonapp.viewmodel.newtask.CreateTaskViewModel
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

/**
 * View model factory
 */
class ViewModelFactory(override val kodein: Kodein)
    : ViewModelProvider.NewInstanceFactory(), KodeinAware {

    private val taskRepository: TaskRepository by instance()
    private val workManager: WorkManager by instance()

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
            with(modelClass) {
                when {
                    isAssignableFrom(TasksViewModel::class.java) ->
                        TasksViewModel(taskRepository, workManager)
                    isAssignableFrom(CreateTaskViewModel::class.java) ->
                        CreateTaskViewModel(taskRepository)
                    else ->
                        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
                }
            } as T
}
