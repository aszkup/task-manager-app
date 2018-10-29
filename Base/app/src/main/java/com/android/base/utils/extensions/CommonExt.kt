package com.android.base.utils.extensions

import android.content.Context
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.android.base.R
import com.android.base.utils.enums.*
import es.dmoral.toasty.Toasty
import timber.log.Timber

/**
 * Log life cycle events
 */
fun lifeCycleDebug(log: String) {
//    if (BuildConfig.LIFE_CYCLE_DEBUG) Timber.d(log)
    if (true) Timber.d(log)
}

/**
 * Find color
 */
fun findColor(context: Context, resourceId: Int) =
        ContextCompat.getColor(context, resourceId)

/**
 * Show toast
 */
fun showToast(context: Context?, @StringRes messageResId: Int, @MessageType messageType: Int) {
    context?.let {
        val message = it.resources.getText(messageResId).toString()
        showToast(it, message, messageType)
    }
}

/**
 * Show toast
 */
fun showToast(context: Context?, message: String, @MessageType messageType: Int) {
    @ColorInt val backgroundColorInt: Int
    @DrawableRes val iconDrawableRes: Int
    when (messageType) {
        GENERAL_ERROR -> {
            backgroundColorInt = R.color.pastelRed
            iconDrawableRes = R.drawable.ic_error
        }
        SERVER_ERROR -> {
            backgroundColorInt = R.color.pastelRed
            iconDrawableRes = R.drawable.ic_error_backend
        }
        NETWORK_ERROR -> {
            backgroundColorInt = R.color.pastelRed
            iconDrawableRes = R.drawable.ic_error_network
        }
        SUCCESS_MESSAGE -> {
            backgroundColorInt = R.color.delicateBlue
            iconDrawableRes = R.drawable.ic_message_success
        }
        GENERAL_MESSAGE -> {
            backgroundColorInt = R.color.delicateBlue
            iconDrawableRes = R.drawable.ic_error
        }
        else -> {
            backgroundColorInt = R.color.delicateBlue
            iconDrawableRes = R.drawable.ic_error
        }
    }

    context?.applicationContext?.let {
        Toasty.custom(it, message, iconDrawableRes, findColor(it, backgroundColorInt),
                Toast.LENGTH_LONG, true, true)
                .show()
    }
}
