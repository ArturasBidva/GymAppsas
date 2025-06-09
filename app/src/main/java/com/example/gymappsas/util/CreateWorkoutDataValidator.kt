package com.example.gymappsas.util
import javax.inject.Inject

class CreateWorkoutDataValidator @Inject constructor() {

    fun validateTitle(workoutTitle: String): FieldError? {
        return if (workoutTitle.isBlank()) {
            FieldError.EMPTY_TITLE
        } else {
            null
        }
    }

    fun validateDescription(workoutDescription: String): FieldError? {
        return if (workoutDescription.isBlank()) {
            FieldError.EMPTY_DESCRIPTION
        } else {
            null
        }
    }
}

enum class FieldError {
    EMPTY_TITLE,
    EMPTY_DESCRIPTION;

    fun toMessage(): String {
        return when (this) {
            EMPTY_TITLE -> "The title cannot be empty."
            EMPTY_DESCRIPTION -> "The description cannot be empty."
        }
    }
}
