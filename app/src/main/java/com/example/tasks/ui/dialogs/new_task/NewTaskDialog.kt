package com.example.tasks.ui.dialogs.new_task

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.tasks.R
import com.example.tasks.databinding.DialogNewTaskBinding
import com.example.tasks.ui.tasks.TasksViewModel
import com.example.tasks.utils.Constants
import com.example.tasks.utils.TaskNotification
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collect
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class NewTaskDialog : BottomSheetDialogFragment() {

    private lateinit var binding: DialogNewTaskBinding
    private val viewModel: TasksViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View {
        isCancelable = false
        binding = DialogNewTaskBinding.inflate(inflater, container, false)
        initialize()
        return binding.root
    }

    private fun initialize() {
        lifecycleScope.launchWhenStarted {
            viewModel.newTaskUiState.collect {
                when (it) {
                    is TasksViewModel.UiState.Loading -> {
                        binding.btnAdd.isEnabled = false
                        binding.btnClose.isEnabled = false
                        binding.etDue.isEnabled = false
                        binding.priority.isEnabled = false
                    }
                    is TasksViewModel.UiState.Error -> {
                        binding.btnAdd.isEnabled = true
                        binding.btnClose.isEnabled = true
                        binding.etDue.isEnabled = true
                        binding.priority.isEnabled = true
                        Snackbar.make(requireView(), it.message, Snackbar.LENGTH_SHORT).show()
                    }
                    is TasksViewModel.UiState.Success -> {
                        findNavController().navigateUp()
                        viewModel.resetNewTaskState()
                    }
                    else -> Unit
                }
            }
        }
        binding.btnClose.setOnClickListener { findNavController().navigateUp() }
        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.support_simple_spinner_dropdown_item,
            Constants.PRIORITIES.values.toList()
        )
        binding.priority.apply {
            setAdapter(adapter)
            setOnItemClickListener { _, _, i, _ ->
                binding.priority.setText(Constants.PRIORITIES.values.toList()[i], false)
            }
        }
        binding.etDue.setOnClickListener { showDatePicker() }
        binding.btnAdd.setOnClickListener { createTask() }
    }

    private fun showDatePicker() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        DatePickerDialog(requireContext(), { _, _year, _month, _day ->
            val date = LocalDate.of(_year, _month + 1, _day)
            binding.etDue.setText(date.format(DateTimeFormatter.ofPattern("y-MM-dd")))
        }, year, month, day).show()
    }

    private fun createTask() {
        val name = binding.etName.text.toString()
        val priority = binding.priority.text?.toString()?.let {
            if (it.isNotEmpty()) Constants.PRIORITIES.filterValues { v -> v == it }.keys.first()
            else null
        }
        val date = binding.etDue.text?.toString()?.let {
            if (it.isNotEmpty()) it else null
        }
        if (name.isNotBlank() && priority != null && date != null) {
            viewModel.createTask(name, priority, date)
            TaskNotification.notify(
                requireContext(),
                "New task created",
                "A new task $name was successfully created"
            )
        }
    }
}