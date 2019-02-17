package com.example.taskmanagerapp.model

import android.net.Uri
import com.example.taskmanagerapp.repository.task.Task

data class CreateTaskViewState(
        val task: Task? = null,
        val fileUri: Uri? = null)
