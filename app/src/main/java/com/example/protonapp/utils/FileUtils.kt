package com.example.protonapp.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.OpenableColumns

class FileUtils(private val context: Context) {

    fun getFileName(fileUri: Uri): String {
        if (fileUri.toString().startsWith(CONTENT_PREFIX)) {
            getFileNameFromContentResolver(fileUri)?.let { return it }
        } else {
            fileUri.lastPathSegment?.let {
                return it.subSequence(it.lastIndexOf("/") + 1, it.length).toString()
            }
        }
        return fileUri.toString()
    }

    private fun getFileNameFromContentResolver(fileUri: Uri): String? {
        val takeFlags = (Intent.FLAG_GRANT_READ_URI_PERMISSION)
        context.contentResolver.takePersistableUriPermission(fileUri, takeFlags)
        val cursor = context.contentResolver.query(fileUri, null, null,
                null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                return cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
            }
        }
        return null
    }

    companion object {
        const val CONTENT_PREFIX = "content://"
    }
}
