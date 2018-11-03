package com.example.protonapp.model

import com.example.protonapp.repository.task.Task

data class CreateTaskViewState(
        val task: Task? = null,
        val taskStored: Boolean? = null)
