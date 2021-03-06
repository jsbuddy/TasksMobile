package com.example.tasks.ui.tasks

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tasks.R
import com.example.tasks.adapters.TaskRecyclerViewAdapter
import com.example.tasks.databinding.FragmentCompletedTasksBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import timber.log.Timber

@AndroidEntryPoint
class CompletedTasksFragment : Fragment(R.layout.fragment_completed_tasks) {

    private lateinit var binding: FragmentCompletedTasksBinding
    private val viewModel: TasksViewModel by activityViewModels()
    private lateinit var tasksAdapter: TaskRecyclerViewAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentCompletedTasksBinding.bind(view)
        Timber.d("Project id: ${viewModel.project}")
        initialize()
    }

    private fun initialize() {
        setupAdapter()
        setupSwipeToDelete()
        lifecycleScope.launchWhenStarted {
            viewModel.tasks().collect {
                tasksAdapter.differ.submitList(it.filter { t -> t.completed })
            }
        }
    }

    private fun setupAdapter() {
        binding.rvCompleted.apply {
            tasksAdapter = TaskRecyclerViewAdapter(requireContext())
            tasksAdapter.setOnCheckChangeLister { task, b -> viewModel.toggleCompleted(task.id, b) }
            adapter = tasksAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setupSwipeToDelete() {
        val callback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN, ItemTouchHelper.LEFT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder,
            ) = true

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val task = tasksAdapter.differ.currentList[position]
                viewModel.deleteTask(task)
                Snackbar.make(requireView(), "Task deleted", Snackbar.LENGTH_SHORT)
                    .apply {
                        setAction("Undo") {
                            viewModel.createTask(task.name, task.priority, task.due)
                        }
                        show()
                    }
            }
        }
        ItemTouchHelper(callback).attachToRecyclerView(binding.rvCompleted)
    }
}