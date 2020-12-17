package com.example.tasks.ui.projects

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tasks.R
import com.example.tasks.adapters.ProjectRecyclerViewAdapter
import com.example.tasks.databinding.FragmentProjectsBinding
import com.example.tasks.ui.MainActivity
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import timber.log.Timber

@AndroidEntryPoint
class ProjectsFragment : Fragment(R.layout.fragment_projects) {

    private lateinit var projectsAdapter: ProjectRecyclerViewAdapter
    private val viewModel: ProjectsViewModel by viewModels()
    private lateinit var binding: FragmentProjectsBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProjectsBinding.bind(view)
        (requireActivity() as MainActivity).supportActionBar?.title = "Projects"
        initialize()
    }

    private fun initialize() {
        lifecycleScope.launchWhenStarted {
            viewModel.projects.collect {
                projectsAdapter.differ.submitList(it)
            }
        }
        binding.rvProjects.apply {
            projectsAdapter = ProjectRecyclerViewAdapter()
            projectsAdapter.setOnItemClickListener {
                Snackbar.make(requireView(), it.name, Snackbar.LENGTH_SHORT)
                    .setBackgroundTint(
                        ContextCompat.getColor(requireContext(), R.color.popupBackground)
                    )
                    .setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                    .show()
            }
            adapter = projectsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }
}