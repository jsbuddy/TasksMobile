package com.example.tasks.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.tasks.data.db.entities.Project
import com.example.tasks.databinding.ListItemProjectBinding

class ProjectRecyclerViewAdapter : RecyclerView.Adapter<ProjectRecyclerViewAdapter.ViewHolder>() {
    inner class ViewHolder(
        private val binding: ListItemProjectBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(project: Project) {
            binding.name.text = project.name
            binding.deadline.text = project.deadline ?: "No deadline"
            binding.root.setOnClickListener {
                onItemClickListener?.let { it(project) }
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
