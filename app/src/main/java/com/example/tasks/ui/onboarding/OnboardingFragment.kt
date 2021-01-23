package com.example.tasks.ui.onboarding

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.tasks.R
import com.example.tasks.data.repositories.AuthRepository
import com.example.tasks.databinding.FragmentOnboardingBinding
import com.example.tasks.utils.show
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class OnboardingFragment : Fragment(R.layout.fragment_onboarding) {

    @Inject
    lateinit var authRepository: AuthRepository

    private lateinit var binding: FragmentOnboardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        checkAuth()
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()
        binding = FragmentOnboardingBinding.bind(view)

        binding.btnStart.setOnClickListener {
            findNavController().navigate(R.id.action_onboardingFragment_to_registerFragment)
        }
    }

    override fun onStart() {
        super.onStart()
        binding.root.show()
    }

    private fun checkAuth() {
        lifecycleScope.launchWhenStarted {
            if (authRepository.checkAuth()) {
                findNavController().navigate(R.id.action_onboardingFragment_to_projectsFragment)
            } else {
                binding.root.show()
                authRepository.logout()
            }
        }
    }
}