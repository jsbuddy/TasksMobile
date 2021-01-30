package com.example.tasks.ui.projects

import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tasks.R
import com.example.tasks.adapters.ProjectRecyclerViewAdapter
import com.example.tasks.databinding.FragmentProjectsBinding
import com.example.tasks.ui.auth.AuthFragment
import com.example.tasks.utils.hide
import com.example.tasks.utils.show
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first

@AndroidEntryPoint
class ProjectsFragment : AuthFragment(R.layout.fragment_projects) {

    private lateinit var projectsAdapter: ProjectRecyclerViewAdapter
    private val viewModel: ProjectsViewModel by activityViewModels()
    private lateinit var binding: FragmentProjectsBinding

    override fun onAuthenticated(view: View) {
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
            viewModel.projectsUiState.collect {
                when (it) {
                    is ProjectsViewModel.UiState.Loading -> showLoading()
                    is ProjectsViewModel.UiState.Success -> {
                        if (viewModel.projects.first().isEmpty()) showEmptyState()
                        else showList()
                        viewModel.resetProjectsUiState()
                    }
                    else -> Unit
                }
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.projects.collect {
                projectsAdapter.differ.submitList(it)
                if (it.isNotEmpty()) showList()
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
        binding.btnNewProject.setOnClickListener {
            findNavController().navigate(R.id.action_projectsFragment_to_newProjectDialog)
        }
    }

    private fun showEmptyState() {
        binding.emptyState.show()
        binding.rvProjects.hide()
        binding.fabNewProject.hide()
        binding.loading.hide()
    }

    private fun showLoading() {
        binding.loading.show()
        binding.emptyState.hide()
        binding.fabNewProject.hide()
    }

    private fun showList() {
        binding.emptyState.hide()
        binding.rvProjects.show()
        binding.fabNewProject.show()
        binding.loading.hide()
    }
}