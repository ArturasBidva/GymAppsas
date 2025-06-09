package com.example.gymappsas.ui.screens.camera

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(
    private val savePhotoToGalleryUseCase: SavePhotoToGalleryUseCase
) : ViewModel() {
    private val _uiState: MutableStateFlow<CameraState> = MutableStateFlow(CameraState())
    val uiState: StateFlow<CameraState> = _uiState

    fun storePhotoInGallery(bitmap: Bitmap) {
        viewModelScope.launch {
            savePhotoToGalleryUseCase.call(bitmap)
            updateCapturedPhotoState(bitmap)
        }
    }

    private fun updateCapturedPhotoState(updatedPhoto: Bitmap?) {
        _uiState.value.capturedImage?.recycle()
        _uiState.value = _uiState.value.copy(capturedImage = updatedPhoto)
    }

    override fun onCleared() {
        _uiState.value.capturedImage?.recycle()
        super.onCleared()
    }
}