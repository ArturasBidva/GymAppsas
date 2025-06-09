package com.example.gymappsas.ui.screens.workoutcreated

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gymappsas.R
import com.example.gymappsas.ui.reusable.lexenBold
import com.example.gymappsas.ui.reusable.lexendRegular

@Composable
fun WorkoutCreated(modifier: Modifier = Modifier) {
    Content()
}

@Composable
private fun Content() {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(218.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.workoutcreated),
                    contentDescription = "Avatar icon",
                    modifier = Modifier.matchParentSize()
                )
            }
            Text(
                "Great job, you've created your workout!",
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 20.dp),
                fontFamily = lexenBold,
                fontSize = 28.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                "Would you like to schedule the workout now?",
                modifier = Modifier
                    .padding(horizontal = 16.dp),
                fontSize = 16.sp,
                fontFamily = lexendRegular
            )
            Spacer(modifier = Modifier.height(20.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Box(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(Color(0xFF0A6BD9))
                        .clickable {}
                ) {
                    Text(
                        "Schedule",
                        fontSize = 16.sp,
                        fontFamily = lexenBold,
                        color = Color.Black,
                        modifier = Modifier
                            .padding(horizontal = 22.dp)
                            .padding(vertical = 12.dp)
                    )
                }
                Box(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(Color(0xFFE5EDF5))
                        .clickable {}
                ) {
                    Text(
                        "Not now",
                        fontSize = 16.sp,
                        fontFamily = lexenBold,
                        color = Color.Black,
                        modifier = Modifier
                            .padding(horizontal = 22.dp)
                            .padding(vertical = 12.dp)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun WorkoutCreatedPreview() {
    Content()
}