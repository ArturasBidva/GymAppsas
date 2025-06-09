package com.example.gymappsas.util

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun TimerComposable(
    modifier: Modifier = Modifier,
    totalTime: Long = 180000L,
    handleColor: Color = Color.Green,
    inactiveBarColor: Color = Color.Green,
    activeBarColor: Color = Color.Gray,
    initialValue: Float = 1f,
    strokeWidth: Dp = 5.dp,
    onTimerFinish: () -> Unit

) {
    var size by remember { mutableStateOf(IntSize.Zero) }
    var value by remember { mutableFloatStateOf(initialValue) }
    var currentTime by remember { mutableLongStateOf(totalTime) }
    var isTimerRunning by remember { mutableStateOf(false) }
    var timerFinished by remember { mutableStateOf(false) }
    LaunchedEffect(key1 = currentTime, key2 = isTimerRunning) {
        if (currentTime > 0 && isTimerRunning) {
            delay(100L)
            currentTime -= 100L
            value = currentTime / totalTime.toFloat()
        } else if (currentTime <= 0 && !timerFinished) {
            timerFinished = true
            onTimerFinish()
            isTimerRunning = false
        }
    }

    Box(
        modifier = Modifier.onSizeChanged {
            size = it
        },
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = modifier) {
            drawArc(
                color = inactiveBarColor,
                startAngle = -215f,
                sweepAngle = 250f,
                useCenter = false,
                size = Size(size.width.toFloat(), size.height.toFloat()),
                style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round)

            )
            drawArc(
                color = activeBarColor,
                startAngle = -215f,
                sweepAngle = 250f * value,
                useCenter = false,
                size = Size(size.width.toFloat(), size.height.toFloat()),
                style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round)

            )
            val center = Offset(size.width / 2f, size.height / 2f)
            val r = size.width / 2f
            rotate(250 * value - 215f, pivot = center) {
                drawPoints(
                    listOf(Offset(center.x + r, center.y)),
                    pointMode = PointMode.Points,
                    color = handleColor,
                    strokeWidth = (strokeWidth * 3f).toPx(),
                    cap = StrokeCap.Round
                )
            }

        }
        Text(
            text = formatTime(currentTime),
            fontSize = 44.sp,
            fontWeight = FontWeight.Bold,
        )
        Button(
            onClick = {
                if (currentTime <= 0L) {
                    currentTime = totalTime
                    isTimerRunning = true
                    timerFinished = false
                } else {
                    isTimerRunning = !isTimerRunning
                }
            }, modifier = Modifier.align(Alignment.BottomCenter),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isTimerRunning || currentTime <= 0L) {
                    Color.Green
                } else {
                    Color.Red
                }
            )
        ) {
            Text(
                text = if (isTimerRunning && currentTime > 0L) "Stop"
                else if (!isTimerRunning && currentTime >= 0L) "Start"
                else "Restart"
            )
        }
    }
}

@Preview
@Composable
private fun TimerPrev() {
    Box(
        modifier = Modifier.padding(horizontal = 20.dp),
        contentAlignment = Alignment.Center
    ) {
        TimerComposable(
            totalTime = 100L * 1000L,
            handleColor = Color.Green,
            inactiveBarColor = Color.DarkGray,
            activeBarColor = Color(0xFF37B900),
            modifier = Modifier.size(200.dp),
            onTimerFinish = {}
        )
    }

}

private fun formatTime(ms: Long): String {
    val minutes = (ms / 1000) / 60
    val seconds = (ms / 1000) % 60
    return String.format("%02d:%02d", minutes, seconds)
}