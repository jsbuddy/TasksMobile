package com.example.tasks.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Task(
    val completed: Boolean,
    val createdAt: String,
    val due: String,
    @PrimaryKey
    @SerializedName("_id")
    val id: String,
    val name: String,
    val priority: Int,
    val project: String,
    val updatedAt: String
)