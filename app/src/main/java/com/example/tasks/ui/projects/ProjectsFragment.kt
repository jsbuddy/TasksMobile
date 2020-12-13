package com.example.tasks.ui.projects

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.tasks.R
import com.example.tasks.databinding.FragmentProjectsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import timber.log.Timber

@AndroidEntryPoint
class ProjectsFragment : Fragment(R.layout.fragment_projects) {

    private val viewModel: ProjectsViewModel by viewModels()
    private lateinit var binding: FragmentProjectsBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProjectsBinding.bind(view)
        initialize()
    }

    private fun initialize() {
        lifecycleScope.launchWhenStarted {
            viewModel.projects.collect {
                Timber.d("Projects $it")
            }
        }
    }
}