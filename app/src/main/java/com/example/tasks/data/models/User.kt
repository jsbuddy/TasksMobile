package com.example.tasks.data.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class User(
    @SerializedName("_id")
    val id: String,
    val name: String,
    val email: String,
) : Serializable