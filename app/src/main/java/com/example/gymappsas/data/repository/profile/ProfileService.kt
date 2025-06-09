package com.example.gymappsas.data.repository.profile

import com.example.gymappsas.data.db.entities.ProfileEntity
import com.example.gymappsas.data.db.models.profile.Profile
import com.example.gymappsas.util.Validator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ProfileService @Inject constructor(private val profileRepository: ProfileRepository) {

    suspend fun saveProfile(profile: Profile) {
        if(!Validator.validateProfile(profile)) {
            throw IllegalArgumentException("Invalid profile data")
        }
        profileRepository.insertProfile(profileEntity = profile.toProfileEntity())
    }

    suspend fun getProfile(): Flow<Profile?> {
        return profileRepository.getProfile().let { profileEntity ->
            profileEntity.map { it?.toProfile() }
        }
    }

    private fun Profile.toProfileEntity(): ProfileEntity {
        return ProfileEntity(
            name = this.name,
            age = this.age,
            joinDate = this.joinDate,
            weeklyTrainingMinutes = this.weeklyTrainingMinutes,
            weight = this.weight,
            height = this.height,
            gender = this.gender
        )
    }

    private fun ProfileEntity.toProfile(): Profile {
        return Profile(
            name = this.name,
            age = this.age,
            joinDate = this.joinDate,
            weeklyTrainingMinutes = this.weeklyTrainingMinutes,
            weight = this.weight,
            height = this.height)
    }

}