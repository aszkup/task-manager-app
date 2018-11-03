package com.example.protonapp.model

import com.example.protonapp.repository.task.Task

data class CreateTaskViewState(
        val tasks: Task,
        val created: Boolean? = null)
