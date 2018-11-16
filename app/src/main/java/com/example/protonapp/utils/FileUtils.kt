package com.example.protonapp.utils

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.OpenableColumns

class FileUtils(private val context: Context) {

    fun getFileName(fileUri: Uri): String {
        if (fileUri.toString().startsWith(CONTENT_PREFIX)) {
            var cursor: Cursor? = null
            try {
                cursor = context.contentResolver.query(fileUri, null,
                        null, null, null)
                if (cursor != null && cursor.moveToFirst()) {
                    return cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                }
            } finally {
                cursor?.close()
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
