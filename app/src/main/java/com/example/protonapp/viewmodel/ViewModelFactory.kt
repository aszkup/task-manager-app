package com.example.protonapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.protonapp.viewmodel.main.TasksViewModel
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.KodeinInjected
import com.github.salomonbrys.kodein.KodeinInjector

/**
 * View model factory
 */
class ViewModelFactory(private val kodein: Kodein)
    : ViewModelProvider.NewInstanceFactory(), KodeinInjected {

    override val injector = KodeinInjector()

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
            with(modelClass) {
                when {
                    isAssignableFrom(TasksViewModel::class.java) ->
                        TasksViewModel()
                    else ->
                        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
                }
            } as T
}
