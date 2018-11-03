package com.example.protonapp.viewmodel.main

import androidx.lifecycle.MutableLiveData
import com.android.base.model.ViewState
import com.android.base.viewmodel.BaseViewModel
import com.example.protonapp.model.TasksViewState

class TasksViewModel : BaseViewModel() {

    val viewState: MutableLiveData<ViewState<TasksViewState>> = MutableLiveData()
}
