package com.example.tasks.ui.tasks

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.tasks.R
import com.example.tasks.adapters.TasksSectionsPagerAdapter
import com.example.tasks.databinding.FragmentTasksBinding
import com.example.tasks.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class TasksFragment : Fragment() {

    private lateinit var binding: FragmentTasksBinding
    private val viewModel: TasksViewModel by activityViewModels()
    private val args: TasksFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (requireActivity() as MainActivity).supportActionBar?.title = ""
        binding = FragmentTasksBinding.inflate(inflater, container, false)
        viewModel.initialize(args.project)
        initialize()
        return binding.root
    }

    private fun initialize() {
        setupViewPager()
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

    private fun setupViewPager() {
        val sectionsPagerAdapter = TasksSectionsPagerAdapter(requireContext(), childFragmentManager)
        binding.viewPager.adapter = sectionsPagerAdapter
        binding.tabs.setupWithViewPager(binding.viewPager)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater.inflate(R.menu.tasks, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_new_task -> {
                findNavController().navigate(R.id.action_tasksFragment_to_newTaskDialog)
            }
            R.id.action_delete_project -> {
                Toast.makeText(requireContext(), "Delete project", Toast.LENGTH_SHORT).show()
            }
        }
        return true
    }
}