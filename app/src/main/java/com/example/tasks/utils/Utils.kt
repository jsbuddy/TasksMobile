package com.example.tasks.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object Utils {
    fun formatDate(string: String): String {
        val date = LocalDateTime.parse(string.replace("Z", ""))
        return date.format(DateTimeFormatter.ofPattern("d, MMM y"))
    }
}