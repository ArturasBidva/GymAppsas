package com.example.gymappsas.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymappsas.data.repository.profile.ProfileService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileService: ProfileService
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> get() = _uiState

    init {
        getProfile()
    }
    private fun getProfile() {
        viewModelScope.launch {
            profileService.getProfile().onStart {
                _uiState.update { it.copy(isLoading = true) }
            }.collect { profile ->
                if (profile != null){
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            username = profile.name,
                            age = profile.age,
                            weeklyWorkoutCount = profile.weeklyTrainingMinutes,
                            gender = profile.gender,
                            joinDate = profile.joinDate

                        )
                    }
                }
               }
            }
        }
    }
