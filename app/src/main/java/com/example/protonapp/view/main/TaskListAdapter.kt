package com.example.protonapp.view.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.protonapp.databinding.ItemTaskBinding
import com.example.protonapp.repository.task.Task

/**
 * Task list adapter
 */
class TaskListAdapter(val tasks: MutableList<Task>)
    : RecyclerView.Adapter<TaskListAdapter.TaskViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemTaskBinding.inflate(inflater)
        return TaskViewHolder(binding)
    }

    override fun getItemCount() = tasks.size

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) =
            holder.bind(tasks[position])

    inner class TaskViewHolder(private val binding: ItemTaskBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(task: Task) {
            binding.task = task
            binding.executePendingBindings()
        }
    }
}
