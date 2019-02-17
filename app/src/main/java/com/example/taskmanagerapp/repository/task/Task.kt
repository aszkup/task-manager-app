package com.example.taskmanagerapp.repository.task

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import org.threeten.bp.Instant
import java.util.*

@Entity(tableName = "tasks")
@Parcelize
data class Task(
        @PrimaryKey
        @ColumnInfo(name = "taskid")
        val id: String = UUID.randomUUID().toString(),
        val name: String,
        val description: String,
        val fileUri: String,
        val keywords: String? = null,
        var createdAt: Instant = Instant.now(),
        val scheduledAt: Instant? = null,
        val startedAt: Instant? = null,
        val finishedAt: Instant? = null
) : Parcelable
