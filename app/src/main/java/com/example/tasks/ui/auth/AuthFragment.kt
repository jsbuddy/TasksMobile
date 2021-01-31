package com.example.tasks.ui.auth

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.runBlocking

abstract class AuthFragment(layout: Int) : Fragment(layout) {

    private val viewModel: AuthViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (checkAuth()) this.onAuthCreated()
    }

    open fun onAuthCreated() {}

    private fun checkAuth(): Boolean = runBlocking {
        return@runBlocking if (viewModel.checkAuth()) true
        else {
            viewModel.logout()
            val uri = Uri.parse("tasksmgr://onboard")
            val options = NavOptions.Builder().setPopUpTo(
                findNavController().graph.startDestination, true
            ).build()
            findNavController().navigate(uri, options)
            false
        }
    }
}