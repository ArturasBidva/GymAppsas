package com.example.gymappsas.util

import java.time.LocalDate
import java.time.LocalTime

fun LocalTime?.toFormattedString() = this?.toString() ?: ""
fun LocalDate?.toFormattedString() = this?.toString() ?: ""
