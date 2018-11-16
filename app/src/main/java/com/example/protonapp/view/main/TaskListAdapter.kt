package com.example.protonapp.view.main

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.protonapp.databinding.ItemTaskBinding
import com.example.protonapp.repository.task.Task
import com.example.protonapp.utils.FileUtils

/**
 * Task list adapter
 */
class TaskListAdapter(private val fileUtils: FileUtils)
    : PagedListAdapter<Task, TaskListAdapter.TaskViewHolder>(TaskDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemTaskBinding.inflate(inflater, parent, false)
        return TaskViewHolder(binding, fileUtils)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    fun getItemAt(position: Int): Task? = getItem(position)

    inner class TaskViewHolder(
            private val binding: ItemTaskBinding,
            private val fileUtils: FileUtils)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(task: Task) {
            binding.task = task
            binding.fileName = fileUtils.getFileName(Uri.parse(task.fileUri))
            binding.executePendingBindings()
        }
    }

    private class TaskDiffCallback : DiffUtil.ItemCallback<Task>() {

        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem == newItem
        }
    }
}
