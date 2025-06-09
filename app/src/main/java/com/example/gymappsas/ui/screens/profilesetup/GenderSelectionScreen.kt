package com.example.gymappsas.ui.screens.profilesetup

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.gymappsas.R
import com.example.gymappsas.ui.AppTheme

@Composable
fun GenderSelectionScreen(
    selectedGender: Gender?,
    onGenderSelected: (Gender) -> Unit,
    onContinue: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                text = "How do you identify?",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            // Wrap in Box to prevent overflow
            Box(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    GenderOption(
                        type = Gender.MALE,
                        icon = R.drawable.male,
                        isSelected = selectedGender == Gender.MALE,
                        onSelect = { onGenderSelected(it) },
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    GenderOption(
                        type = Gender.FEMALE,
                        icon = R.drawable.females,
                        isSelected = selectedGender == Gender.FEMALE,
                        onSelect = onGenderSelected,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            FilledTonalButton(
                onClick = onContinue,
                enabled = selectedGender != null,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Continue")
            }

            TextButton(onClick = { /* Handle skip */ }) {
                Text("Prefer not to say")
            }
        }
    }
}


@Composable
fun GenderOption(
    type: Gender,
    icon: Int,
    isSelected: Boolean,
    onSelect: (Gender) -> Unit,
    modifier: Modifier = Modifier
) {
    val iconSize = 120.dp
    val paddingSize = 16.dp

    // Animations
    val borderColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.primary
        else Color.Transparent,
        animationSpec = tween(300),
        label = "borderColor"
    )

    Box(
        modifier = modifier
            .animateContentSize()
            .shadow(4.dp, MaterialTheme.shapes.medium)
            .border(2.dp, borderColor, MaterialTheme.shapes.medium)
            .clip(MaterialTheme.shapes.medium)
            .clickable { onSelect(type) }
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .padding(paddingSize),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = type.displayName,
                modifier = Modifier.size(iconSize),
                tint = Color.Unspecified
            )
            Text(
                text = type.displayName,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}


@Preview
@Composable
private fun GenderSelectionScreenPreview() {
    AppTheme {
        GenderSelectionScreen(selectedGender = Gender.MALE, onGenderSelected = {}) { }
    }
}
@Preview
@Composable
private fun GenderSelectionScreenDarkPreview() {
    AppTheme(darkTheme = true) {
        GenderSelectionScreen(selectedGender = Gender.MALE, onGenderSelected = {}) { }
    }
}

