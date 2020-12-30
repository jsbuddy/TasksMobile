package com.example.tasks.utils

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
}