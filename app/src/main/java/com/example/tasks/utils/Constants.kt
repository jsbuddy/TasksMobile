package com.example.tasks.utils

object Constants {
    const val DATABASE_NAME = "projects.db"
    const val BASE_URL = "https://taskshead.herokuapp.com/api/"
    val PRIORITIES = hashMapOf(
        1 to "Low",
        2 to "Normal",
        3 to "High",
        4 to "Urgent"
    )
}