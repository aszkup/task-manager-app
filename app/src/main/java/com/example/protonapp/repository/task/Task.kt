package com.example.protonapp.repository.task

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task_table")
data class Task(
        @PrimaryKey
        @NonNull val id: String,
        val name: String,
        val description: String
)
