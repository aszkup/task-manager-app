package com.android.base.utils.extensions

import android.view.View
import android.widget.EditText
import androidx.core.content.ContextCompat

/**
 * Extension method to get text as string from EditText
 */
val EditText.value
    get() = text.toString()

/**
 * Set background color
 *
 * @param colorId color id
 */
fun View.setBgColor(colorId: Int) =
        this.setBackgroundColor(ContextCompat.getColor(this.context, colorId))

/**
 * Set visibility to Visible
 */
fun View.visible() {
    if (visibility != View.VISIBLE) {
        visibility = View.VISIBLE
    }
}

/**
 * Set visibility to Invisible
 */
fun View.invisiable() {
    if (visibility != View.INVISIBLE) {
        visibility = View.INVISIBLE
    }
}

/**
 * Set visibility to Gone
 */
fun View.gone() {
    if (visibility != View.GONE) {
        visibility = View.GONE
    }
}
