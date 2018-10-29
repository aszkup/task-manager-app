package com.android.base.utils.enums

import androidx.annotation.IntDef

/**
 * Message Type
 */
@kotlin.annotation.Retention(AnnotationRetention.SOURCE)
@IntDef(GENERAL_MESSAGE, SUCCESS_MESSAGE, GENERAL_ERROR, SERVER_ERROR, NETWORK_ERROR)
annotation class MessageType

/**
 * General positive message
 */
const val GENERAL_MESSAGE = 0

/**
 * Success positive message
 */
const val SUCCESS_MESSAGE = 1

/**
 * General error message
 */
const val GENERAL_ERROR = 2

/**
 * Server error message
 */
const val SERVER_ERROR = 3

/**
 * Network error message
 */
const val NETWORK_ERROR = 4
