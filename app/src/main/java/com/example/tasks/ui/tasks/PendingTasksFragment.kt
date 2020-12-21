package com.example.tasks.ui.tasks

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tasks.R
import com.example.tasks.adapters.TaskRecyclerViewAdapter
import com.example.tasks.databinding.FragmentPendingTasksBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map

@AndroidEntryPoint
class PendingTasksFragment : Fragment(R.layout.fragment_pending_tasks) {

    private lateinit var binding: FragmentPendingTasksBinding
    private val viewModel: TasksViewModel by activityViewModels()
    private lateinit var tasksAdapter: TaskRecyclerViewAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentPendingTasksBinding.bind(view)
        initialize()
    }

    private fun initialize() {
        binding.rvPending.apply {
            tasksAdapter = TaskRecyclerViewAdapter(requireContext())
            tasksAdapter.setOnCheckChangeLister { task, b -> viewModel.toggleCompleted(task.id, b) }
            adapter = tasksAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        lifecycleScope.launchWhenStarted {
            viewModel.tasks().collect {
                tasksAdapter.differ.submitList(it.filter { t -> !t.completed })
            }
        }
    }
}
