package com.example.protonapp.repository.task

import androidx.room.TypeConverter
import org.threeten.bp.Instant
import org.threeten.bp.format.DateTimeFormatter

class Converters {
    companion object {
        private val formatter = DateTimeFormatter.ISO_INSTANT

        @TypeConverter
        @JvmStatic
        fun toInstant(value: String?): Instant? =
                value?.let { return formatter.parse(value, Instant::from) }

        @TypeConverter
        @JvmStatic
        fun fromInstant(date: Instant?): String? =
                date?.let { return formatter.format(it) }
    }
}
