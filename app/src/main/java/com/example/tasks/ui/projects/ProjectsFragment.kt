package com.example.tasks.ui.projects

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tasks.R
import com.example.tasks.adapters.ProjectRecyclerViewAdapter
import com.example.tasks.databinding.FragmentProjectsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class ProjectsFragment : Fragment(R.layout.fragment_projects) {

    private lateinit var projectsAdapter: ProjectRecyclerViewAdapter
    private val viewModel: ProjectsViewModel by activityViewModels()
    private lateinit var binding: FragmentProjectsBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProjectsBinding.bind(view)
        initialize()
        setupToolbar()
    }

    private fun setupToolbar() {
        binding.toolbar.apply {
            title = "Projects"
            setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.action_settings -> {
                        findNavController().navigate(R.id.action_projectsFragment_to_settingsFragment)
                        true
                    }
                    R.id.action_logout -> {
                        viewModel.logout()
                        findNavController().navigate(R.id.action_projectsFragment_to_onboardingFragment)
                        true
                    }
                    else -> true
                }
            }
            inflateMenu(R.menu.projects)
        }
    }

    private fun initialize() {
        viewModel.fetchProjects()
        lifecycleScope.launchWhenStarted {
            viewModel.projects.collect {
                projectsAdapter.differ.submitList(it)
            }
        }
        binding.rvProjects.apply {
            projectsAdapter = ProjectRecyclerViewAdapter(requireContext())
            projectsAdapter.setOnItemClickListener {
                findNavController().navigate(
                    ProjectsFragmentDirections.actionProjectsFragmentToTasksFragment3(it)
                )
            }
            adapter = projectsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        binding.fabNewProject.setOnClickListener {
            findNavController().navigate(R.id.action_projectsFragment_to_newProjectDialog)
        }
    }
}