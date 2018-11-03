package com.example.protonapp.repository.task

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "tasks")
data class Task(
        @PrimaryKey
        @ColumnInfo(name = "taskid")
        val id: String = UUID.randomUUID().toString(),
        val name: String,
        val description: String
)
