package com.example.gymappsas.data.repository.profile

import com.example.gymappsas.data.db.entities.ProfileEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProfileRepository @Inject constructor(private val profileDao: ProfileDao) {
    suspend fun insertProfile(profileEntity: ProfileEntity) {
        profileDao.insertProfile(profileEntity)
    }
    suspend fun getProfile(): Flow<ProfileEntity?> {
        return profileDao.getProfile()
    }
}