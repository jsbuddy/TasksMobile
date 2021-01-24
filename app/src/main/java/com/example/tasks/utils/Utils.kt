package com.example.tasks.utils

import android.content.Context
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.google.android.material.snackbar.Snackbar
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object Utils {
    fun formatDate(string: String): String {
        val date = LocalDateTime.parse(string.replace("Z", ""))
        return date.format(DateTimeFormatter.ofPattern("d, MMM y"))
    }

    fun isToday(date: String): Boolean {
        val parsed = LocalDateTime.parse(date.replace("Z", ""))
        val today = LocalDateTime.now()
        return parsed.dayOfMonth == today.dayOfMonth
    }

    fun showSnackBar(view: View, message: String) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
    }

    fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isPasswordValid(password: String): Boolean {
        return password.length >= 8
    }
}

fun View.hideKeyboard() {
    val inputMethodManager = context.getSystemService(
        Context.INPUT_METHOD_SERVICE
    ) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(this.windowToken, 0)
}

fun View.enable() {
    isEnabled = true
    alpha = 1f
}

fun View.disable() {
    isEnabled = false
    alpha = .5f
}

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}