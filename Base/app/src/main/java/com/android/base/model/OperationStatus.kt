package com.android.base.model

import com.android.base.utils.BaseMessage

/**
 * Operation status class
 */
sealed class OperationStatus {
    override fun toString(): String {
        return this::class.java.simpleName
    }
}

/**
 * Operation status Idle class (start status)
 */
class Idle : OperationStatus()

/**
 * Operation status Success class
 */
data class Success<out T>(val data: T? = null) : OperationStatus()

/**
 * Operation status InProgress class
 */
class InProgress : OperationStatus()

/**
 * Operation status OperationError class
 */
open class OperationError(open val message: BaseMessage) : OperationStatus() {
    override fun toString(): String {
        return "${this::class.java.simpleName}=$message"
    }
}

/**
 * Operation status Unauthorized class
 */
data class Unauthorized(override val message: BaseMessage = BaseMessage.Unauthorized()) : OperationError(message)

/**
 * Operation status Timeout class
 */
data class Timeout(override val message: BaseMessage = BaseMessage.Timeout()) : OperationError(message)
