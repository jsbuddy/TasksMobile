package com.example.tasks.ui.tasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tasks.adapters.TaskRecyclerViewAdapter
import com.example.tasks.adapters.TasksSectionsPagerAdapter
import com.example.tasks.databinding.FragmentTasksBinding
import com.example.tasks.ui.MainActivity
import com.google.android.material.badge.BadgeDrawable
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import timber.log.Timber

@AndroidEntryPoint
class TasksFragment : Fragment() {

    private lateinit var binding: FragmentTasksBinding
    private val viewModel: TasksViewModel by activityViewModels()
    private val args: TasksFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (requireActivity() as MainActivity).supportActionBar?.title = ""
        binding = FragmentTasksBinding.inflate(inflater, container, false)
        viewModel.initialize(args.project)
        setupViewPager()
        initialize()
        return binding.root
    }

    private fun setupViewPager() {
        val sectionsPagerAdapter = TasksSectionsPagerAdapter(requireContext(), childFragmentManager)
        binding.viewPager.adapter = sectionsPagerAdapter
        binding.tabs.setupWithViewPager(binding.viewPager)
    }

    private fun initialize() {
        binding.name.text = args.project.name
        lifecycleScope.launchWhenStarted {
            viewModel.tasks().collect {
                binding.tabs.getTabAt(0)?.apply {
                    text = "Pending (${it.filter { t -> !t.completed }.size})"
                }
                binding.tabs.getTabAt(1)?.apply {
                    text = "Completed (${it.filter { t -> t.completed }.size})"
                }
            }
        }
    }
}