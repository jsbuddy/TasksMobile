package com.example.tasks.ui.auth

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.tasks.data.repositories.AuthRepository
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

abstract class AuthFragment(layout: Int) : Fragment(layout) {

    @Inject
    lateinit var authRepository: AuthRepository

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (checkAuth()) onAuthenticated(view)
    }

    abstract fun onAuthenticated(view: View)

    private fun checkAuth(): Boolean = runBlocking {
        return@runBlocking if (authRepository.checkAuth()) true
        else {
            authRepository.logout()
            val uri = Uri.parse("tasksmgr://onboard")
            val options = NavOptions.Builder().setPopUpTo(
                findNavController().graph.startDestination, true
            ).build()
            findNavController().navigate(uri, options)
            false
        }
    }
}