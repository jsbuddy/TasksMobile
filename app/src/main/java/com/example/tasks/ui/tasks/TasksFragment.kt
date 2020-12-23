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
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

private val TAB_TITLES = arrayOf(
    R.string.tab_text_pending,
    R.string.tab_text_completed
)

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
        savedInstanceState: Bundle?,
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
        binding.viewPager.adapter = TasksSectionsPagerAdapter(this)
        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
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
                deleteProject()
            }
        }
        return true
    }

    private fun deleteProject() {
        MaterialAlertDialogBuilder(requireContext())
            .setMessage("Are you sure you want to delete this project including all pending and completed tasks? This action cannot be undone.")
            .setNegativeButton("No") { _, _ -> }
            .setPositiveButton("Yes") { _, _ ->
                viewModel.deleteProject()
                findNavController().navigateUp()
            }
            .show()
    }
}