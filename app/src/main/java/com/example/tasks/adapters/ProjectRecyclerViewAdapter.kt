package com.example.tasks.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.tasks.R
import com.example.tasks.data.db.entities.Project
import com.example.tasks.databinding.ListItemProjectBinding
import com.example.tasks.utils.Utils
import kotlin.math.roundToInt

class ProjectRecyclerViewAdapter(
    private val context: Context,
) : RecyclerView.Adapter<ProjectRecyclerViewAdapter.ViewHolder>() {
    inner class ViewHolder(
        private val binding: ListItemProjectBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(project: Project) {
            binding.apply {
                name.text = project.name
                val totalTasks = project.completedTasksCount + project.pendingTasksCount
                if (totalTasks > 0) {
                    val p = ((project.completedTasksCount.toDouble() / totalTasks.toDouble()) * 100)
                        .roundToInt()
                    progress.setProgressCompat(p, true)
                    if (p in 0..39) progress.setIndicatorColor(context.getColor(R.color.red))
                    if (p in 40..99) progress.setIndicatorColor(context.getColor(R.color.orange))
                    if (p == 100) progress.setIndicatorColor(context.getColor(R.color.green))
                    count.text = context.getString(
                        R.string.task_count, project.completedTasksCount, totalTasks
                    )
                } else {
                    progress.visibility = View.GONE
                    count.text = context.getString(R.string.no_task)
                }
                if (!project.deadline.isNullOrBlank() && project.deadline.isNotEmpty()) {
                    deadline.text = context.getString(
                        R.string.deadline_val, Utils.formatDate(project.deadline)
                    )
                } else deadline.text = context.getString(R.string.no_deadline)
                root.setOnClickListener {
                    onItemClickListener?.let { it(project) }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemProjectBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        return holder.bind(differ.currentList[position])
    }

    override fun getItemCount() = differ.currentList.size

    private val callback = object : DiffUtil.ItemCallback<Project>() {
        override fun areItemsTheSame(old: Project, new: Project) = old.id == new.id

        override fun areContentsTheSame(old: Project, new: Project) = old == new
    }

    val differ = AsyncListDiffer(this, callback)

    private var onItemClickListener: ((Project) -> Unit)? = null

    fun setOnItemClickListener(listener: (Project) -> Unit) {
        onItemClickListener = listener
    }
}
