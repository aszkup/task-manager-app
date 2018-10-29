package com.android.base.utils.extensions

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent

/**
 * Get component
 */
fun Context.componentFor(targetType: Class<*>): ComponentName =
        ComponentName(this, targetType)

/**
 * Start activity
 */
fun Context.startActivity(f: Intent.() -> Unit): Unit =
        Intent().apply(f).run(this::startActivity)

/**
 * Start component
 */
inline fun <reified T : Activity> Context.start(noinline f: Intent.() -> Unit = {}) =
        startActivity {
            component = componentFor(T::class.java)
            f(this)
        }

/**
 * Start component
 */
fun Context.start(a: String, f: Intent.() -> Unit = {}) =
        startActivity {
            action = a
            f(this)
        }