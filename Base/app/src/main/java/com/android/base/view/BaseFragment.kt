package com.android.base.view

import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import com.android.base.model.OperationError
import com.android.base.model.ViewState
import com.android.base.utils.extensions.lifeCycleDebug
import org.kodein.di.KodeinAware
import org.kodein.di.android.support.closestKodein
import org.kodein.di.generic.instance
import timber.log.Timber

/**
 * Base fragment
 */
abstract class BaseFragment : Fragment(), KodeinAware {

    override val kodein by closestKodein()
    private val fragmentTag: String = this::class.java.simpleName
    private val viewModelFactory: ViewModelProvider.Factory by instance()

    init {
        lifeCycleDebug("$fragmentTag:init ${this}")
    }

    open fun onBackPressed() = false

    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
    fun logLifeCycle() {
        Timber.d("$tag: ${lifecycle.currentState}  ${this}")
    }

    /**
     * Get proper [ViewModel] based on [ViewModel] class passed as argument
     *
     * @param modelClass class
     * @return viewModel proper view model
     */
    protected fun <T : ViewModel> getModel(modelClass: Class<T>) =
            ViewModelProviders.of(this, viewModelFactory).get(modelClass)

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
        (activity as BaseActivity).viewStateUpdate(viewState, onNewState, showInProgress, hideInProgress, showError)
    }
}
