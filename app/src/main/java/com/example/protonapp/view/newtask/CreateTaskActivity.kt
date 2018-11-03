package com.example.protonapp.view.newtask

import android.os.Bundle
import androidx.lifecycle.Observer
import com.android.base.view.BaseActivity
import com.example.protonapp.R
import com.example.protonapp.viewmodel.newtask.CreateTaskViewModel

class CreateTaskActivity : BaseActivity() {

    private lateinit var viewModel: CreateTaskViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_create)

        viewModel = getModel(CreateTaskViewModel::class.java)
        viewModel.viewState.observe(this, Observer {
        })
    }
}
