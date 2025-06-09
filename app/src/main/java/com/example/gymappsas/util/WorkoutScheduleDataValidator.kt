package com.example.gymappsas.util
import com.example.gymappsas.data.db.models.workouts.Workout
import java.time.LocalTime
import javax.inject.Inject

class WorkoutScheduleDataValidator @Inject constructor() {

    fun validateTimes(
        startTime: LocalTime?,
        endTime: LocalTime?
    ): Pair<Result<Unit, DateError>, Result<Unit, DateError>> {
        val validatedStartTime = validateStartTime(startTime)
        val validatedEndTime = validateEndTime(endTime)
        return Pair(validatedStartTime, validatedEndTime)
    }

    private fun validateStartTime(time: LocalTime?): Result<Unit, DateError> {
        if (time == null) {
            return Result.Error(DateError.NO_START_TIME)
        }
        return Result.Success(Unit)
    }

    private fun validateEndTime(time: LocalTime?): Result<Unit, DateError> {
        if (time == null) {
            return Result.Error(DateError.NO_END_TIME)
        }
        return Result.Success(Unit)
    }

    fun validateWorkout(workoutLocal: Workout?): Result<Unit, DateError> {
        if (workoutLocal?.title.isNullOrEmpty()) {
            return Result.Error(DateError.NO_WORKOUT_SELECTED)
        }
        return Result.Success(Unit)
    }

    enum class DateError : Error {
        NO_START_TIME,
        NO_END_TIME,
        NO_WORKOUT_SELECTED
    }
}