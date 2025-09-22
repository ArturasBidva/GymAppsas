package com.example.gymappsas.util

import com.example.gymappsas.data.db.models.profile.Profile

object MockProfileData {

    val mockProfile = Profile(
        id = 1,
        name = "John Doe",
        age = 25,
        weight = 70F,
        height = 175F,
        bmi = 25F)

}