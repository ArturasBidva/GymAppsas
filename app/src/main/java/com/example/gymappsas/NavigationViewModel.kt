package com.example.gymappsas

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class NavigationViewModel : ViewModel() {
    private val _selectedTab = MutableStateFlow(0)
    val selectedTab = _selectedTab.asStateFlow()

    fun syncTabWithRoute(route: String) {
        val index = when (route) {
            "Home" -> 0
            "Profile" -> 1
            "Settings" -> 2
            "Other" -> 3
            else -> return
        }
        _selectedTab.value = index
    }

}