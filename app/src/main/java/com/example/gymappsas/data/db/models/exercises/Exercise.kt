package com.example.gymappsas.data.db.models.exercises

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Exercise(
    val id: Long = 0,
    val name: String,
    val images: List<String>,
    val instructions: List<String>,
    val primaryMuscles: List<String>


): Parcelable