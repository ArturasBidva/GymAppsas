package com.example.gymappsas.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.gymappsas.data.db.Converters
import com.example.gymappsas.data.db.GymDatabase
import com.example.gymappsas.data.repository.completedworkout.CompletedWorkoutDao
import com.example.gymappsas.data.repository.exercise.ExerciseDao
import com.example.gymappsas.data.repository.exerciseworkout.ExerciseWorkoutDao
import com.example.gymappsas.data.repository.profile.ProfileDao
import com.example.gymappsas.data.repository.schedule.ScheduleDao
import com.example.gymappsas.data.repository.workout.WorkoutDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
@TypeConverters(Converters::class)
object RoomModule {
    @Singleton
    @Provides
    fun provideGymDatabase(
        @ApplicationContext appContext: Context
    ): GymDatabase {
        return Room.databaseBuilder(
            appContext,
            GymDatabase::class.java,
            "gym.db"
        ).addCallback(object : RoomDatabase.Callback() {
            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                db.execSQL("PRAGMA foreign_keys=ON;")
            }
        }).build()
    }

    @Singleton
    @Provides
    fun provideWorkoutDao(database: GymDatabase): WorkoutDao {
        return database.workoutDao()
    }

    @Singleton
    @Provides
    fun provideScheduleDao(database: GymDatabase): ScheduleDao {
        return database.scheduleDao()
    }

    @Singleton
    @Provides
    fun provideCompletedWorkoutDao(database: GymDatabase): CompletedWorkoutDao {
        return database.completedWorkoutDao()
    }

    @Singleton
    @Provides
    fun provideExerciseWorkoutDao(database: GymDatabase): ExerciseWorkoutDao {
        return database.exerciseWorkoutDao()
    }

    @Singleton
    @Provides
    fun provideExerciseDao(database: GymDatabase): ExerciseDao {
        return database.exerciseDao()
    }

    @Singleton
    @Provides
    fun provideProfileDao(database: GymDatabase): ProfileDao {
        return database.profileDao()
    }

}