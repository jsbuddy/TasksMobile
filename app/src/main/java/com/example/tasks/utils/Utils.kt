package com.example.tasks.utils

import android.content.Context
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
}

fun View.hideKeyboard() {
    val inputMethodManager = context.getSystemService(
        Context.INPUT_METHOD_SERVICE
    ) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(this.windowToken, 0)
}

fun View.enable() {
    isEnabled = true
}

fun View.disable() {
    isEnabled = false
}

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}