package com.example.gymappsas.data.db.models.profile

import android.os.Parcelable
import com.example.gymappsas.ui.screens.profilesetup.Gender
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Parcelize
data class Profile(
    val id: Long = 0,
    val name: String = "",
    val age: Int = 0,
    val joinDate: String = formatedDate(),
    val weeklyTrainingMinutes: Int = 0,
    val weight: Float = 0f,
    val height: Float = 0f,
    val gender: Gender = Gender.NONE

)
    : Parcelable
fun formatedDate(): String {
    // Get the current date and time
    val current = LocalDateTime.now()

    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    val formatted = current.format(formatter)

    return formatted
}