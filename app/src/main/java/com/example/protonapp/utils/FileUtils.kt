package com.example.protonapp.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.OpenableColumns
import timber.log.Timber

class FileUtils(private val context: Context) {

    fun getFileName(fileUri: Uri): String {
        if (fileUri.toString().startsWith(CONTENT_PREFIX)) {
            try {
                val takeFlags = (Intent.FLAG_GRANT_READ_URI_PERMISSION)
                context.contentResolver.takePersistableUriPermission(fileUri, takeFlags)
                val cursor = context.contentResolver.query(fileUri, null, null,
                        null, null, null)
                cursor?.use {
                    if (it.moveToFirst()) {
                        return cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                    }
                }
            } catch (exception: Exception) {
                Timber.e(exception)
            }
        } else {
            fileUri.lastPathSegment?.let {
                return it.subSequence(it.lastIndexOf("/") + 1, it.length).toString()
            }
        }
        return fileUri.toString()
    }

    companion object {
        const val CONTENT_PREFIX = "content://"
    }
}
