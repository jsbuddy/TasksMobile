package com.example.tasks.ui.onboarding

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.tasks.R
import com.example.tasks.databinding.FragmentOnboardingBinding

class OnboardingFragment : Fragment(R.layout.fragment_onboarding) {

    private lateinit var binding: FragmentOnboardingBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentOnboardingBinding.bind(view)
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()

        binding.btnStart.setOnClickListener {
            findNavController().navigate(R.id.action_onboardingFragment_to_projectsFragment)
        }
    }
}