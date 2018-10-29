package com.android.base.viewmodel

import androidx.lifecycle.ViewModel
import com.android.base.model.*
import com.android.base.utils.BaseMessage
import com.android.base.utils.extensions.lifeCycleDebug
import io.reactivex.disposables.CompositeDisposable
import retrofit2.HttpException
import timber.log.Timber
import java.net.ConnectException
import java.net.HttpURLConnection
import java.net.SocketTimeoutException

/**
 * Base view model
 */
open class BaseViewModel : ViewModel() {

    private val tag: String = this::class.java.simpleName
    protected val disposables = CompositeDisposable()

    init {
        lifeCycleDebug("$tag:init ${this}")
    }

    override fun onCleared() {
        lifeCycleDebug("$tag:onCleared ${this}")
        disposables.dispose()
        super.onCleared()
    }

    /**
     * Handle IO error
     *
     * @param throwable [Throwable] instance
     * @return [ViewState] instance
     */
    protected fun <T> onError(throwable: Throwable, model: T?): ViewState<T> {
        Timber.e(throwable, "$tag: error: ${throwable.message}")

        fun getViewState(data: T? = model, operationStatus: OperationStatus?) =
                ViewState(model = data, status = operationStatus)

        fun getOperationErrorViewState() =
                getViewState(operationStatus = OperationError(BaseMessage.BackendError()))

        return when (throwable) {
            is HttpException -> {
                when (throwable.code()) {
                    HttpURLConnection.HTTP_UNAUTHORIZED -> {
                        getViewState(data = getModelUnauthorized(model), operationStatus = Unauthorized())
                    }
                    else -> getOperationErrorViewState()
                }
            }
            is ConnectException, is SocketTimeoutException -> getViewState(operationStatus = Timeout())
            else -> getOperationErrorViewState()
        }
    }

    /**
     * Get proper model for Unauthorized error
     *
     * @param model
     * @return model
     */
    open fun <T> getModelUnauthorized(model: T?): T? {
        return model
    }
}
