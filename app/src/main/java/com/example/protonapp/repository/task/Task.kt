package com.example.protonapp.repository.task

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.threeten.bp.Instant
import java.util.*

@Entity(tableName = "tasks")
data class Task(
        @PrimaryKey
        @ColumnInfo(name = "taskid")
        val id: String = UUID.randomUUID().toString(),
        val name: String,
        val description: String,
        val state: String,
        val fileUri: String,
        var createdAt: Instant = Instant.now(),
        val startedAt: Instant? = null,
        val finishedAt: Instant? = null
)
