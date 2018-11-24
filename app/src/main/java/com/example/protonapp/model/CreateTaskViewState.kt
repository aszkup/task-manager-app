package com.example.protonapp.model

import android.net.Uri
import com.example.protonapp.repository.task.Task

data class CreateTaskViewState(
        val task: Task? = null,
        val fileUri: Uri? = null)
