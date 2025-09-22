package com.example.gymappsas

import android.app.Application
import android.content.Context
import com.example.gymappsas.data.repository.workoutvariants.WorkoutVariantsRepository
import com.example.gymappsas.data.repository.workoutvariants.WorkoutVariantsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun bindContext(application: Application): Context

    @Binds
    @Singleton
    abstract fun bindWorkoutVariantsRepository(
        workoutVariantsRepositoryImpl: WorkoutVariantsRepositoryImpl
    ): WorkoutVariantsRepository
}