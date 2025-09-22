package com.example.gymappsas.data.repository.fitness

import com.example.gymappsas.data.db.dao.StepsDao
import com.example.gymappsas.data.db.models.profile.Profile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

interface FitnessRepository {
    suspend fun getTodayFitnessData(profile : Profile): Flow<FitnessData>
    suspend fun getWeeklyFitnessData(): Flow<List<FitnessData>>
}

@Singleton
class FitnessRepositoryImpl @Inject constructor(
    private val stepsDao: StepsDao
) : FitnessRepository {

    override suspend fun getTodayFitnessData(profile: Profile): Flow<FitnessData> =
        stepsDao.getStepsFlowForDate(todayString()).map { stepsEntity ->
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val steps = stepsEntity?.steps ?: 0
            val distanceInMeters = calculateDistanceFromSteps(steps, profile.height)
            val distanceInKm = distanceInMeters / 1000.0
            FitnessData(
                steps = stepsEntity?.steps ?: 0,
                calories = 520f,
                distance = distanceInKm.toFloat(),
                activeMinutes = 45,
                heartRate = 72,
                date = dateFormat.format(Date())
            )
        }

    override suspend fun getWeeklyFitnessData(): Flow<List<FitnessData>> =
        stepsDao.getWeeklyStepsFlow().map { stepsList ->
            stepsList.map { stepsEntity ->
                FitnessData(
                    steps = stepsEntity.steps,
                    calories = Random.nextFloat() * 400 + 200,
                    distance = Random.nextFloat() * 3.5f + 1.5f,
                    activeMinutes = Random.nextInt(20, 60),
                    heartRate = Random.nextInt(65, 85),
                    date = stepsEntity.date
                )
            }
        }

    private fun todayString(): String =
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
}
    fun calculateDistanceFromSteps(steps: Int, heightCm: Float, isRunning: Boolean = false): Double {
        if (heightCm <= 0f) return 0.0

        val factor = if (isRunning) 0.65f else 0.415f
        val strideLengthMeters = heightCm * factor / 100f
        return steps * strideLengthMeters.toDouble()
    }