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
import com.example.protonapp.utils.WorkManagerUtils
import org.threeten.bp.Duration


/**
 * Task list adapter
 */
class TaskListAdapter(
        private val fileUtils: FileUtils,
        private val workManagerUtils: WorkManagerUtils,
        private val clickListener: (Task) -> Unit)
    : PagedListAdapter<Task, TaskListAdapter.TaskViewHolder>(TaskDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemTaskBinding.inflate(inflater, parent, false)
        return TaskViewHolder(binding, fileUtils, workManagerUtils, clickListener)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    fun getItemAt(position: Int): Task? = getItem(position)

    inner class TaskViewHolder(
            private val binding: ItemTaskBinding,
            private val fileUtils: FileUtils,
            private val workManagerUtils: WorkManagerUtils,
            private val clickListener: (Task) -> Unit)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(task: Task) {
            binding.task = task
            binding.fileName = fileUtils.getFileName(Uri.parse(task.fileUri))
            task.finishedAt?.let {
                binding.isFinished = true
                binding.duration = printDuration(Duration.between(task.startedAt, task.finishedAt))
            }
            binding.isInProgress = workManagerUtils.isWorkScheduled(task.id) or
                    workManagerUtils.isWorkRunning(task.id)
            binding.executePendingBindings()
            itemView.setOnClickListener { clickListener(task) }
        }

        private fun printDuration(duration: Duration): String {
            val stringBuilder = StringBuilder()
            val hours = duration.toHours()
            hours.takeIf { it > 0 }?.apply { stringBuilder.append(hours).append("h ") }
            val minutes = duration.toMinutes() % 60
            minutes.takeIf { it > 0 }?.apply { stringBuilder.append(hours).append("m ") }
            stringBuilder.append(duration.seconds % 60).append("s")
            return stringBuilder.toString()
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
