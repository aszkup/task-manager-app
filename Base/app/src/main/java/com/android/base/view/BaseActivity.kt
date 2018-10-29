package com.android.base.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import com.android.base.BaseApplication
import com.android.base.model.*
import com.android.base.utils.enums.GENERAL_ERROR
import com.android.base.utils.extensions.lifeCycleDebug
import com.android.base.utils.extensions.showToast
import com.android.base.view.login.BaseAuthActivity
import com.github.salomonbrys.kodein.KodeinInjector
import com.github.salomonbrys.kodein.android.AppCompatActivityInjector
import com.github.salomonbrys.kodein.instance
import timber.log.Timber

/**
 * Base activity
 */
abstract class BaseActivity : AppCompatActivity(), AppCompatActivityInjector {

    override val injector = KodeinInjector()
    private val viewModelFactory: ViewModelProvider.Factory by instance()
    private val tag: String = this::class.java.simpleName

    init {
        lifeCycleDebug("$tag:init ${this}")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        initializeInjector()
        super.onCreate(savedInstanceState)
    }

    override fun onDestroy() {
        destroyInjector()
        super.onDestroy()
    }

    override fun onBackPressed() {
        var handled = false
        supportFragmentManager.fragments.filterIsInstance<BaseFragment>().forEach {
            handled = it.onBackPressed()
            if (handled) return@forEach
        }
        if (!handled) super.onBackPressed()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
    fun logLifeCycle() {
        Timber.d("$tag: ${lifecycle.currentState}  ${this}")
    }

    /**
     * Update UI when new [ViewState] is passed
     *
     * @param viewState new view state
     * @param onNewState on new state lambda
     * @param showInProgress show in progress
     * @param hideInProgress hide in progress
     * @param showError show error
     */
    fun <T> viewStateUpdate(viewState: ViewState<T>?,
                            onNewState: (T) -> Unit,
                            showInProgress: () -> Unit,
                            hideInProgress: () -> Unit,
                            showError: (OperationError) -> Unit) {
        viewState?.let {
            it.model?.let(onNewState)
            onNewStatus(it.status, showInProgress, hideInProgress, showError)
        }
    }

    /**
     * Update UI when new [ViewState] is passed
     *
     * @param viewState new view state
     * @param onNewState on new state lambda
     * @param showInProgress show in progress
     * @param hideInProgress hide in progress
     * @param showError show error
     */
    protected fun <T> viewStateUpdated(viewState: ViewState<T>?,
                                       onNewState: (T) -> Unit = {},
                                       showInProgress: () -> Unit = {},
                                       hideInProgress: () -> Unit = {},
                                       showError: (OperationError) -> Unit = {}) {
        Timber.d("${this::class.java.simpleName}: updated view state: {$viewState}")
        viewStateUpdate(viewState, onNewState, showInProgress, hideInProgress, showError)
    }

    /**
     * Get proper [ViewModel] based on [ViewModel] class passed as argument
     *
     * @param modelClass class
     * @param viewModelFactory [ViewModelFactory]
     * @return viewModel proper view model
     */
    protected fun <T : ViewModel> getModel(modelClass: Class<T>) =
            ViewModelProviders.of(this, viewModelFactory).get(modelClass)

    private fun onNewStatus(operationStatus: OperationStatus?,
                            showInProgress: () -> Unit,
                            hideInProgress: () -> Unit,
                            showError: (OperationError) -> Unit) {
        operationStatus?.let {
            when (operationStatus) {
                is Idle -> Timber.d("Operation status: Idle")
                is Success<*> -> Timber.d("Operation status: Success")
                is InProgress -> showInProgress()
                is Unauthorized -> onUnauthorized(operationStatus, hideInProgress)
                is Timeout -> onError(operationStatus, hideInProgress, showError)
                else -> run {
                    onError(operationStatus, hideInProgress, showError)
                }
            }
        }
    }

    private fun onError(operationStatus: OperationStatus?,
                        hideInProgress: () -> Unit,
                        showError: (OperationError) -> Unit) {
        hideInProgress()
        showError(operationStatus as OperationError)
    }

    private fun onUnauthorized(unauthorized: Unauthorized,
                               hideInProgress: () -> Unit) {
        hideInProgress()
        if (this !is BaseAuthActivity) {
            showToast(this, unauthorized.message.messageId!!, unauthorized.message.type)
            finish()
            startActivity(Intent(this, ((application as BaseApplication).getLoginActivityClass())))
        }
    }

    protected fun showErrorMessage(operationError: OperationError) {
        operationError.message.messageId?.let {
            showToast(this, it, GENERAL_ERROR)
        }
        operationError.message.message?.let {
            showToast(this, it, GENERAL_ERROR)
        }
    }
}
