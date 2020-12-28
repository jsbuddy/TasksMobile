package com.example.tasks.ui.dialogs

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.tasks.databinding.DialogNewProjectBinding
import com.example.tasks.ui.projects.ProjectsViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@AndroidEntryPoint
class NewProjectDialog : BottomSheetDialogFragment() {

    private val viewModel: ProjectsViewModel by activityViewModels()
    private lateinit var binding: DialogNewProjectBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        isCancelable = false
        binding = DialogNewProjectBinding.inflate(inflater, container, false)
        initialize()
        return binding.root
    }

    private fun initialize() {
        lifecycleScope.launchWhenStarted {
            viewModel.newProjectUiState.collect {
                when (it) {
                    is ProjectsViewModel.UiState.Loading -> {
                        binding.etName.isEnabled = false
                        binding.btnCreate.isEnabled = false
                        binding.btnClose.isEnabled = false
                    }
                    is ProjectsViewModel.UiState.Error -> {
                        binding.etName.isEnabled = true
                        binding.btnCreate.isEnabled = true
                        binding.btnClose.isEnabled = true
                        Snackbar.make(requireView(), it.message, Snackbar.LENGTH_SHORT).show()
                    }
                    is ProjectsViewModel.UiState.Success -> {
                        findNavController().navigateUp()
                    }
                    else -> Unit
                }
            }
        }
        binding.btnClose.setOnClickListener { dismiss() }
        binding.etDeadline.setOnClickListener { showDatePicker() }
        binding.btnCreate.setOnClickListener {
            binding.etName.text?.toString()?.let {
                if (it.isNotEmpty()) viewModel.createProject(it, binding.etDeadline.text.toString())
            }
        }
    }

    private fun showDatePicker() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        DatePickerDialog(requireContext(), { _, _year, _month, _day ->
            val date = LocalDate.of(_year, _month + 1, _day)
            binding.etDeadline.setText(date.format(DateTimeFormatter.ofPattern("y-MM-dd")))
        }, year, month, day).show()
    }
}