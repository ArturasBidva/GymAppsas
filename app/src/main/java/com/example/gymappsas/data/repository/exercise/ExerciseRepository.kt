package com.example.gymappsas.data.repository.exercise

import android.content.Context
import android.util.Log
import com.example.gymappsas.data.db.entities.ExerciseEntity
import com.example.gymappsas.data.db.models.exercises.Exercise
import com.example.gymappsas.data.repository.exercise.ExerciseService.ExerciseJson
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.io.InputStreamReader
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExerciseRepository @Inject constructor(
    private val context: Context,
    private val exerciseDao: ExerciseDao
) {

    private val _exercises = MutableStateFlow<List<Exercise>>(listOf())
    val exercises = _exercises.asStateFlow()

    suspend fun loadExercisesToStateFlow () {
        val gson = Gson()
        val mainDir = "exercisesbycategories"
        val filesToScan = mutableListOf<String>()

        // Recursive function to scan directories and collect file paths
        fun scanDirectory(path: String) {
            val files = context.assets.list(path) ?: return
            files.forEach { file ->
                val fullPath = "$path/$file"
                if (file.endsWith(".json")) {
                    filesToScan.add(fullPath) // Add JSON file to the list
                } else {
                    scanDirectory(fullPath) // Recursively scan directories
                }
            }
        }

        // Start scanning from the main directory
        scanDirectory(mainDir)

        // Process each JSON file and add to exercisesList
        val exercises = mutableListOf<Exercise>()
        for (filePath in filesToScan) {
            try {
                context.assets.open(filePath).use { inputStream ->
                    val reader = InputStreamReader(inputStream)
                    val exerciseJson = gson.fromJson(reader, ExerciseJson::class.java)
                    val exercise = Exercise(
                        id = exerciseJson.id.hashCode().toLong(),
                        name = exerciseJson.name,
                        images = exerciseJson.images,
                        instructions = exerciseJson.instructions,
                        primaryMuscles = listOf(exerciseJson.primaryMuscles.first()))
                    exerciseDao.insertExercise(exerciseEntity = exercise.ToExerciseEntity())
                    exercises.add(exercise)
                }
            } catch (e: Exception) {
                Log.e("ExerciseLoader", "Error loading exercise from $filePath: ${e.message}")
            }

        }
        _exercises.update {
            exercises
        }
    }

    fun Exercise.ToExerciseEntity() : ExerciseEntity{
        return ExerciseEntity(id = this.id)
    }

}