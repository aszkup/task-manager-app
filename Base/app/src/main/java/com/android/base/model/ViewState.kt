package com.android.base.model

/**
 * View state
 */
open class ViewState<out T> @JvmOverloads constructor(
        val model: T? = null,
        val status: OperationStatus? = null) {

    override fun toString(): String {
        return "Model: ${model.toString()}, Status: ${status.toString()}"
    }
}
