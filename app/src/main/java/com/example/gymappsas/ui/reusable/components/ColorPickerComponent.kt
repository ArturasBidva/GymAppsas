package com.example.gymappsas.ui.reusable.components

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.gymappsas.ui.reusable.quicksandMedium

@Composable
fun ColorBubble(
    color: Color,
    isSelected: Boolean,
    onColorSelected: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(20.dp)
            .background(color = color, shape = CircleShape)
            .clickable { onColorSelected.invoke() }
            .border(
                width = if (isSelected) 2.dp else 0.dp,
                color = Color.Black,
                shape = CircleShape
            )
    )
}

@Composable
fun ColorPicker(onColorSelect: (Color) -> Unit) {
    val colors = listOf(Color.Red, Color.Green, Color.Blue, Color.Yellow)
    val selectedColor = remember { mutableStateOf<Color?>(null) }

    Log.d("MyTag", "Selected color: $selectedColor")

    Box(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(50.dp)
            .border(
                border = BorderStroke(1.dp, Color(0xFFEDF1F7)),
                shape = RoundedCornerShape(10.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween, // Aligns items to the start and end
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Select color:",
                color = Color.Gray.copy(alpha = 0.7f),
                fontFamily = quicksandMedium,
                modifier = Modifier.weight(1f) // Allows the text to take minimum space
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                for (color in colors) {
                    ColorBubble(
                        color = color,
                        isSelected = color == selectedColor.value,
                        onColorSelected = {
                            selectedColor.value = color
                            onColorSelect(color)
                        }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun ColorPickerPrev() {
    ColorPicker({})
}