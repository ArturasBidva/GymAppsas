package com.example.gymappsas.di

import com.example.gymappsas.data.repository.fitness.FitnessRepository
import com.example.gymappsas.data.repository.fitness.FitnessRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class FitnessModule {

    @Binds
    @Singleton
    abstract fun bindFitnessRepository(
        fitnessRepositoryImpl: FitnessRepositoryImpl
    ): FitnessRepository
}