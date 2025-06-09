package com.example.gymappsas.data.repository.schedule

import com.example.gymappsas.data.db.entities.ScheduleEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject

class ScheduleRepository @Inject constructor(
    private val scheduleDao: ScheduleDao
) {
    suspend fun insertSchedule(schedule: ScheduleEntity) {
            scheduleDao.insertSchedule(schedule = schedule)
    }

    fun getAllSchedules(): Flow<List<ScheduleEntity>> {
       return scheduleDao.getAllSchedules()
    }

    suspend fun deleteScheduleById(date: LocalDate, workoutId: Long) {
        scheduleDao.deleteSchedulesByDateAndWorkoutId(date = date, workoutId = workoutId)
    }
}