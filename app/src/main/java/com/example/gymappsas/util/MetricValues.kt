package com.example.gymappsas.util

val ageOptions = (16..99).map { it.toString() }
val weightOptions = (45..125).map { it.toString() }
val heightOptions = (150..210).map { it.toString() }

enum class PickerType {
    AGE, WEIGHT, HEIGHT, NONE
}