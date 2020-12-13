package com.example.tasks.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Project(
    val createdAt: String,
    @PrimaryKey(autoGenerate = false)
    @SerializedName("_id")
    val id: String,
    val name: String,
    val updatedAt: String
)