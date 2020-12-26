package com.example.tasks.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.tasks.R
import com.example.tasks.data.db.entities.Project
import com.example.tasks.databinding.ListItemProjectBinding
import com.example.tasks.utils.Utils

class ProjectRecyclerViewAdapter(
    private val context: Context,
) : RecyclerView.Adapter<ProjectRecyclerViewAdapter.ViewHolder>() {
    inner class ViewHolder(
        private val binding: ListItemProjectBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(project: Project) {
            binding.apply {
                name.text = project.name
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
