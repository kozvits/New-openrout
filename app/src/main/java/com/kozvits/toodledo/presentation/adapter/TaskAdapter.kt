package com.kozvits.toodledo.presentation.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kozvits.toodledo.R
import com.kozvits.toodledo.databinding.ItemTaskBinding
import com.kozvits.toodledo.domain.model.Priority
import com.kozvits.toodledo.domain.model.Task
import com.kozvits.toodledo.util.formatDate

class TaskAdapter(
    private val onTaskClick: (Task) -> Unit,
    private val onCheckClick: (Task) -> Unit
) : ListAdapter<Task, TaskAdapter.TaskViewHolder>(DIFF_CALLBACK) {

    fun getTaskAt(pos: Int): Task = getItem(pos)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class TaskViewHolder(private val binding: ItemTaskBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(task: Task) {
            binding.tvTitle.text = task.title
            binding.tvNote.text = task.note.takeIf { it.isNotBlank() } ?: ""

            // Due date
            if (task.dueDate > 0) {
                binding.tvDueDate.text = formatDate(task.dueDate)
                binding.tvDueDate.visibility = android.view.View.VISIBLE
                // Просроченная задача — красный
                val now = System.currentTimeMillis() / 1000L
                binding.tvDueDate.setTextColor(
                    if (task.dueDate < now) Color.parseColor("#FF5252")
                    else Color.parseColor("#B0BEC5")
                )
            } else {
                binding.tvDueDate.visibility = android.view.View.GONE
            }

            // Priority indicator
            val priorityColor = when (task.priority) {
                Priority.TOP, Priority.HIGH -> Color.parseColor("#FF5252")
                Priority.MEDIUM -> Color.parseColor("#FFC107")
                Priority.LOW -> Color.parseColor("#4CAF50")
                else -> Color.parseColor("#455A64")
            }
            binding.priorityBar.setBackgroundColor(priorityColor)

            // Folder & context chips
            if (task.folderName.isNotBlank()) {
                binding.chipFolder.text = task.folderName
                binding.chipFolder.visibility = android.view.View.VISIBLE
            } else {
                binding.chipFolder.visibility = android.view.View.GONE
            }

            if (task.contextName.isNotBlank()) {
                binding.chipContext.text = task.contextName
                binding.chipContext.visibility = android.view.View.VISIBLE
            } else {
                binding.chipContext.visibility = android.view.View.GONE
            }

            // Star / Hot indicators
            binding.ivStar.visibility = if (task.star) android.view.View.VISIBLE else android.view.View.GONE
            binding.ivHot.visibility = if (task.isHot) android.view.View.VISIBLE else android.view.View.GONE

            // Tags
            if (task.tag.isNotBlank()) {
                binding.tvTags.text = "# ${task.tag}"
                binding.tvTags.visibility = android.view.View.VISIBLE
            } else {
                binding.tvTags.visibility = android.view.View.GONE
            }

            binding.root.setOnClickListener { onTaskClick(task) }
            binding.btnComplete.setOnClickListener { onCheckClick(task) }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Task>() {
            override fun areItemsTheSame(a: Task, b: Task) = a.id == b.id
            override fun areContentsTheSame(a: Task, b: Task) = a == b
        }
    }
}
