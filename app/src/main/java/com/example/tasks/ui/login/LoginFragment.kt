package com.example.tasks.ui.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.tasks.R
import com.example.tasks.databinding.FragmentLoginBinding
import com.example.tasks.utils.Utils
import com.example.tasks.utils.disable
import com.example.tasks.utils.enable
import com.example.tasks.utils.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {

    private val viewModel: LoginViewModel by viewModels()
    private lateinit var binding: FragmentLoginBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentLoginBinding.bind(view)
        initialize()
    }

    private fun initialize() {
        lifecycleScope.launchWhenStarted {
            viewModel.uiState.collect {
                when (it) {
                    is LoginViewModel.UiState.FormError -> handleFormError(it)
                    is LoginViewModel.UiState.Error -> handleLoginError(it.message)
                    is LoginViewModel.UiState.Success -> handleLoginSuccess()
                    is LoginViewModel.UiState.Loading -> disableInteraction()
                    else -> Unit
                }
            }
        }

        val afterTextChangedListener = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                viewModel.loginDataChanged(
                    binding.email.text.toString(), binding.password.text.toString()
                )
            }
        }

        binding.email.addTextChangedListener(afterTextChangedListener)
        binding.password.addTextChangedListener(afterTextChangedListener)
        binding.login.setOnClickListener { login() }
        binding.register.setOnClickListener { findNavController().navigate(R.id.action_loginFragment_to_registerFragment) }
        binding.container.setOnFocusChangeListener { _view, b -> if (b) _view.hideKeyboard() }
    }

    private fun login() {
        binding.root.hideKeyboard()
        viewModel.login(binding.email.text.toString(), binding.password.text.toString())
    }

    private fun handleLoginSuccess() {
        findNavController().navigate(R.id.action_loginFragment_to_projectsFragment)
    }

    private fun handleLoginError(message: String) {
        Utils.showError(requireView(), message, requireContext())
        enableInteraction()
    }

    private fun handleFormError(state: LoginViewModel.UiState.FormError) {
        binding.login.isEnabled = state.valid
        state.email?.let { value -> binding.email.error = getString(value) }
        state.password?.let { value -> binding.password.error = getString(value) }
    }

    private fun disableInteraction() {
        binding.loading.show()
        binding.email.disable()
        binding.password.disable()
        binding.login.disable()
        binding.register.disable()
    }

    private fun enableInteraction() {
        binding.loading.hide()
        binding.email.enable()
        binding.password.enable()
        binding.login.enable()
        binding.register.enable()
    }
}