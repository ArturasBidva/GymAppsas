package com.example.gymappsas.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.gymappsas.ui.screens.profilesetup.Gender

@Entity(tableName = "profile")
class ProfileEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val weight: Float,
    val height: Float,
    val age: Int,
    val weeklyTrainingMinutes: Int,
    val joinDate: String,
    val gender: Gender? = null,
    val selectedTrainingDays: List<String> = emptyList(),
    val selectedTrainingLevel: String? = null,
    val selectedFitnessGoal: String? = null,
    val bmi : Float
) {
    override fun toString(): String {
        return "com.example.gymappsas.data.db.entities.ProfileEntity(name=$name, weight=$weight, height=$height, age=$age, weeklyTrainingMinutes=$weeklyTrainingMinutes, joinDate=$joinDate)"
    }
}