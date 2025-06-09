package com.example.gymappsas.data.repository.schedule

import android.util.Log
import com.example.gymappsas.data.db.entities.ScheduleEntity
import com.example.gymappsas.data.db.models.schedules.Schedule
import com.example.gymappsas.data.db.models.workouts.Workout
import com.example.gymappsas.data.repository.workout.WorkoutService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

class ScheduleService @Inject constructor(
    private val scheduleRepository: ScheduleRepository,
    private val workoutService: WorkoutService
) {
    suspend fun insertSchedule(schedule: Schedule) {
        val scheduleEntity = schedule.toScheduleEntity()
        scheduleRepository.insertSchedule(scheduleEntity)
    }


    suspend fun deleteScheduleById(date: LocalDate, workoutId: Long) {
        println("Deleting com.example.gymappsas.data.db.models.schedules.Schedule: date=$date, workoutId=$workoutId")
        scheduleRepository.deleteScheduleById(date = date, workoutId = workoutId)
    }

    fun getAllSchedules(): Flow<List<Schedule>> {
        return scheduleRepository.getAllSchedules()
            .map { scheduleEntities ->
                scheduleEntities.map { it.toSchedule(::getWorkoutById) }
            }
    }

    private suspend fun getWorkoutById(workoutId: Long): Workout? {
        val workouts = workoutService.getAllWorkoutsSus()
        val workout = workouts.firstOrNull { it.id == workoutId }
        Log.e("Amogus", "Workout fetched: $workout")
        return workout
    }


    private fun Schedule.toScheduleEntity(): ScheduleEntity {
        return ScheduleEntity(
            workoutId = this.workout.id,
            date = this.date,
            startTime = this.startTime,
            endTime = this.endTime,
            color = this.color
        )
    }

    private suspend fun ScheduleEntity.toSchedule(getWorkoutById: suspend (Long) -> Workout?): Schedule {
        val workout = getWorkoutById(this.workoutId) ?: Workout()
        return Schedule(
            workout = workout,
            date = this.date,
            startTime = this.startTime,
            endTime = this.endTime,
            color = this.color
        )
    }
}