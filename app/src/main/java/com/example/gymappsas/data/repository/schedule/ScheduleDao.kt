package com.example.gymappsas.data.repository.schedule
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.gymappsas.data.db.entities.ScheduleEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface ScheduleDao {

    @Upsert
    suspend fun insertSchedule(schedule: ScheduleEntity)

    @Upsert
    suspend fun insertSchedules(schedules: List<ScheduleEntity>)

    @Transaction
    @Query("SELECT * FROM schedules")
    fun getAllSchedules(): Flow<List<ScheduleEntity>>

    @Query("DELETE FROM schedules WHERE date = :date AND workoutId = :workoutId")
    suspend fun deleteSchedulesByDateAndWorkoutId(date: LocalDate, workoutId: Long)

    @Transaction
    @Query("SELECT * FROM schedules WHERE date = :date AND workoutId = :workoutId")
    suspend fun getSchedulesByDateAndWorkoutId(date: LocalDate, workoutId: Long): ScheduleEntity?
}
