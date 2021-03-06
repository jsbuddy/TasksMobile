package com.example.tasks.ui.register

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.tasks.R
import com.example.tasks.databinding.FragmentRegisterBinding
import com.example.tasks.utils.Utils
import com.example.tasks.utils.disable
import com.example.tasks.utils.enable
import com.example.tasks.utils.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class RegisterFragment : Fragment(R.layout.fragment_register) {

    private val viewModel: RegisterViewModel by viewModels()
    private lateinit var binding: FragmentRegisterBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentRegisterBinding.bind(view)
        initialize()
    }

    private fun initialize() {
        lifecycleScope.launchWhenStarted {
            viewModel.uiState.collect {
                when (it) {
                    is RegisterViewModel.UiState.FormError -> handleFormError(it)
                    is RegisterViewModel.UiState.Error -> handleRegisterError(it.message)
                    is RegisterViewModel.UiState.Success -> handleRegisterSuccess()
                    is RegisterViewModel.UiState.Loading -> disableInteraction()
                    else -> Unit
                }
            }
        }

        val afterTextChangedListener = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                viewModel.registerDataChanged(
                    binding.name.text.toString(),
                    binding.email.text.toString(),
                    binding.password.text.toString(),
                    binding.confirmPassword.text.toString()
                )
            }
        }

        binding.email.addTextChangedListener(afterTextChangedListener)
        binding.password.addTextChangedListener(afterTextChangedListener)
        binding.confirmPassword.addTextChangedListener(afterTextChangedListener)
        binding.register.setOnClickListener { register() }
        binding.login.setOnClickListener { findNavController().navigate(R.id.action_registerFragment_to_loginFragment) }
        binding.container.setOnFocusChangeListener { _view, b -> if (b) _view.hideKeyboard() }
    }

    private fun register() {
        binding.root.hideKeyboard()
        viewModel.register(
            binding.name.text.toString(),
            binding.email.text.toString(),
            binding.password.text.toString()
        )
    }

    private fun handleRegisterSuccess() {
        findNavController().navigate(R.id.action_registerFragment_to_projectsFragment)
    }

    private fun handleRegisterError(message: String) {
        Utils.showError(requireView(), message, requireContext())
        enableInteraction()
    }

    private fun handleFormError(state: RegisterViewModel.UiState.FormError) {
        binding.register.isEnabled = state.valid
        state.name?.let { value -> binding.name.error = getString(value) }
        state.email?.let { value -> binding.email.error = getString(value) }
        state.password?.let { value -> binding.password.error = getString(value) }
        state.confirmPassword?.let { value -> binding.confirmPassword.error = getString(value) }
    }

    private fun disableInteraction() {
        binding.loading.show()
        binding.name.disable()
        binding.email.disable()
        binding.password.disable()
        binding.confirmPassword.disable()
        binding.register.disable()
        binding.login.disable()
    }

    private fun enableInteraction() {
        binding.loading.hide()
        binding.name.enable()
        binding.email.enable()
        binding.password.enable()
        binding.confirmPassword.enable()
        binding.register.enable()
        binding.login.enable()
    }
}
