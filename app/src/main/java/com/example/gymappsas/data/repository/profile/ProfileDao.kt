package com.example.gymappsas.data.repository.profile

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.example.gymappsas.data.db.entities.ProfileEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProfileDao {
    // Correct upsert operation (no query needed)
    @Upsert
    suspend fun upsertProfile(profile: ProfileEntity)

    // If you specifically want to update just the name
    @Query("UPDATE Profile SET name = :name WHERE id = :userId")
    suspend fun updateName(userId: Long, name: String)

    // If you want to insert with explicit parameters
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: ProfileEntity): Long


    @Query("SELECT * FROM profile")
    fun getProfile(): Flow<ProfileEntity?>
}