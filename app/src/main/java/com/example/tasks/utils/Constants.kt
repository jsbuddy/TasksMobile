package com.example.tasks.utils

import java.util.*

object Constants {
    const val DATABASE_NAME = "projects.db"
    const val BASE_URL = "https://tasksmgr.herokuapp.com/api/"
    val PRIORITIES: MutableMap<Int, String> = Collections.unmodifiableMap(
        hashMapOf(
            1 to "Low",
            2 to "Normal",
            3 to "High",
            4 to "Urgent"
        )
    )
    const val NOTIFICATION_CHANNEL_ID = "com.example.tasks"
    const val NOTIFICATION_CHANNEL_NAME = "Reminder"
}