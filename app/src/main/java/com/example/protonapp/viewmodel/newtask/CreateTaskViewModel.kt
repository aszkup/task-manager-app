package com.example.protonapp.viewmodel.newtask

import androidx.lifecycle.MutableLiveData
import com.android.base.model.ViewState
import com.android.base.viewmodel.BaseViewModel
import com.example.protonapp.model.CreateTaskViewState
import com.example.protonapp.repository.task.TaskRepository

class CreateTaskViewModel(
        private val taskRepository: TaskRepository
) : BaseViewModel() {

    val viewState: MutableLiveData<ViewState<CreateTaskViewState>> = MutableLiveData()

}
