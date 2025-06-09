package com.example.gymappsas.ui.screens.workoutprep

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

class PickerState(
    initialIndex: Int,
    private val items: List<String>
) {
    var selectedIndex by mutableIntStateOf(initialIndex)
        private set

    val selectedItem: String
        get() = items.getOrNull(selectedIndex) ?: ""

    fun updateSelectedIndex(newIndex: Int) {
        selectedIndex = newIndex.coerceIn(0, items.lastIndex)
    }
}

@Composable
fun rememberPickerState(
    initialIndex: Int = 0,
    items: List<String> = emptyList()
): PickerState = remember(items) {
    val safeInitialIndex = if (items.isNotEmpty()) {
        initialIndex.coerceIn(0, items.lastIndex)
    } else {
        0
    }
    PickerState(
        initialIndex = safeInitialIndex,
        items = items
    )
}