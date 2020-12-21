package com.example.tasks.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.tasks.R
import com.example.tasks.data.db.entities.Task
import com.example.tasks.databinding.ListItemTaskBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class TaskRecyclerViewAdapter(private val context: Context) :
    RecyclerView.Adapter<TaskRecyclerViewAdapter.ViewHolder>() {
    inner class ViewHolder(
        private val binding: ListItemTaskBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(task: Task) {
            binding.name.text = task.name
            binding.completed.isChecked = task.completed
            val date = LocalDateTime.parse(task.due.replace("Z", ""))
            val formatted = date.format(DateTimeFormatter.ofPattern("d, MMM y"))
            binding.due.text = formatted
            when (task.priority) {
                1 -> binding.priority.apply {
                    text = context.getString(R.string.low)
                }
                2 -> binding.priority.apply {
                    text = context.getString(R.string.normal)
                    if (!task.completed) setChipBackgroundColorResource(R.color.blue)
                }
                3 -> binding.priority.apply {
                    text = context.getString(R.string.high)
                    if (!task.completed) setChipBackgroundColorResource(R.color.orange)
                }
                4 -> binding.priority.apply {
                    text = context.getString(R.string.urgent)
                    if (!task.completed) setChipBackgroundColorResource(R.color.red)
                }
                else -> Unit
            }
            binding.completed.setOnCheckedChangeListener { view, b ->
                if (view.isPressed) onCheckChangeListener?.let { it(task, b) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemTaskBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        return holder.bind(differ.currentList[position])
    }

    override fun getItemCount() = differ.currentList.size

    private val callback = object : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(old: Task, new: Task) = old.id == new.id

        override fun areContentsTheSame(old: Task, new: Task) = old == new
    }

    val differ = AsyncListDiffer(this, callback)

    private var onCheckChangeListener: ((Task, Boolean) -> Unit)? = null

    fun setOnCheckChangeLister(listener: (Task, Boolean) -> Unit) {
        onCheckChangeListener = listener
    }
}
