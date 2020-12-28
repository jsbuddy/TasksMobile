package com.example.tasks.ui.projects

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tasks.R
import com.example.tasks.adapters.ProjectRecyclerViewAdapter
import com.example.tasks.databinding.FragmentProjectsBinding
import com.example.tasks.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class ProjectsFragment : Fragment(R.layout.fragment_projects) {

    private lateinit var projectsAdapter: ProjectRecyclerViewAdapter
    private val viewModel: ProjectsViewModel by viewModels()
    private lateinit var binding: FragmentProjectsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProjectsBinding.bind(view)
        (requireActivity() as MainActivity).supportActionBar?.title = "Projects"
        initialize()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater.inflate(R.menu.projects, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> {
                findNavController().navigate(R.id.action_projectsFragment_to_settingsFragment)
            }
        }
        return true
    }

    private fun initialize() {
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