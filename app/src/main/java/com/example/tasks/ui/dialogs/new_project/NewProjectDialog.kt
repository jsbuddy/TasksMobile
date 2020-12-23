package com.example.tasks.ui.dialogs.new_project

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.tasks.databinding.DialogNewProjectBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class NewProjectDialog : BottomSheetDialogFragment() {

    private val viewModel: NewProjectViewModel by viewModels()
    private lateinit var binding: DialogNewProjectBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        isCancelable = false
        binding = DialogNewProjectBinding.inflate(inflater, container, false)
        initialize()
        return binding.root
    }

    private fun initialize() {
        lifecycleScope.launchWhenStarted {
            viewModel.uiState.collect {
                when (it) {
                    is NewProjectViewModel.UiState.Loading -> {
                        binding.etName.isEnabled = false
                        binding.btnCreate.isEnabled = false
                        binding.btnClose.isEnabled = false
                    }
                    is NewProjectViewModel.UiState.Error -> {
                        binding.etName.isEnabled = true
                        binding.btnCreate.isEnabled = true
                        binding.btnClose.isEnabled = true
                        Snackbar.make(requireView(), it.message, Snackbar.LENGTH_SHORT).show()
                    }
                    is NewProjectViewModel.UiState.Success -> {
                        findNavController().navigateUp()
                    }
                    else -> Unit
                }
            }
        }
        binding.btnClose.setOnClickListener { dismiss() }
        binding.btnCreate.setOnClickListener {
            binding.etName.text?.toString()?.let {
                if (it.isNotEmpty()) viewModel.create(it)
            }
        }
    }
}