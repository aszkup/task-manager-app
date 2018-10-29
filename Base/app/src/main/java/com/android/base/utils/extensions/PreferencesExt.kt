package com.android.base.utils.extensions

import android.annotation.SuppressLint
import android.content.SharedPreferences

/**
 * Store value with apply
 */
@SuppressLint("ApplySharedPref")
fun SharedPreferences.put(key: String, value: Any, commit: Boolean = false) {
    val editor = edit().let {
        when (value) {
            is String -> it.putString(key, value)
            is Int -> it.putInt(key, value)
            is Boolean -> it.putBoolean(key, value)
            is Long -> it.putLong(key, value)
            is Float -> it.putFloat(key, value)
            else -> error("Only primitive types can be stored in SharedPreferences")
        }
    }
    if (commit) {
        editor.commit()
    } else {
        editor.apply()
    }
}

/**
 * Remove value
 */
fun SharedPreferences.remove(key: String) = this.edit().remove(key).apply()

/**
 * Clear all values
 */
fun SharedPreferences.clearAll() = this.edit().clear().apply()
