package com.example.gymappsas.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.gymappsas.data.db.dao.StepsDao
import com.example.gymappsas.data.db.entities.CompletedWorkoutEntity
import com.example.gymappsas.data.db.entities.ExerciseEntity
import com.example.gymappsas.data.db.entities.ExerciseWorkoutEntity
import com.example.gymappsas.data.db.entities.ProfileEntity
import com.example.gymappsas.data.db.entities.ScheduleEntity
import com.example.gymappsas.data.db.entities.WorkoutAndExerciseWorkoutCrossRef
import com.example.gymappsas.data.db.entities.WorkoutEntity
import com.example.gymappsas.data.db.entities.WorkoutVariantEntity
import com.example.gymappsas.data.db.models.steps.StepsEntity
import com.example.gymappsas.data.repository.completedworkout.CompletedWorkoutDao
import com.example.gymappsas.data.repository.exercise.ExerciseDao
import com.example.gymappsas.data.repository.exerciseworkout.ExerciseWorkoutDao
import com.example.gymappsas.data.repository.profile.ProfileDao
import com.example.gymappsas.data.repository.schedule.ScheduleDao
import com.example.gymappsas.data.repository.workout.WorkoutDao
import com.example.gymappsas.data.repository.workoutvariants.WorkoutVariantDao

@Database(
    entities = [
        WorkoutEntity::class,
        ExerciseWorkoutEntity::class,
        WorkoutAndExerciseWorkoutCrossRef::class,
        ScheduleEntity::class,
        CompletedWorkoutEntity::class,
        ExerciseEntity::class,
        ProfileEntity::class,
        WorkoutVariantEntity::class,
        StepsEntity::class
    ],
    version = 5,
    exportSchema = false,
)
@TypeConverters(Converters::class)
abstract class GymDatabase : RoomDatabase() {
    abstract fun workoutDao(): WorkoutDao
    abstract fun scheduleDao(): ScheduleDao
    abstract fun completedWorkoutDao(): CompletedWorkoutDao
    abstract fun exerciseWorkoutDao(): ExerciseWorkoutDao
    abstract fun exerciseDao(): ExerciseDao
    abstract fun profileDao(): ProfileDao
    abstract fun workoutVariantDao(): WorkoutVariantDao
    abstract fun stepsDao(): StepsDao
}