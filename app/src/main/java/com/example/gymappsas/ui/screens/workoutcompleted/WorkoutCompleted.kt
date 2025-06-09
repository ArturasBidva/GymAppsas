import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.gymappsas.R


@Composable
fun WorkoutCompleted(modifier: Modifier = Modifier) {
    WorkoutCompletedScreen(
        totalTime = 3600000,
        totalExercises = 10,
        totalSets = 30,
        onFinishClick = {})
}

@Composable
fun WorkoutCompletedScreen(
    totalTime: Long,
    totalExercises: Int,
    totalSets: Int,
    onFinishClick: () -> Unit
) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        visible = true
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Celebration Icon
            Icon(
                painter = painterResource(R.drawable.vector), // Replace with your icon
                contentDescription = "Workout Completed",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(120.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))

            // Title
            Text(
                text = "Workout Completed!",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Subtitle
            Text(
                text = "Great job! You've crushed your workout",
                style = MaterialTheme.typography.titleMedium.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Metrics Grid
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    AnimatedVisibility(
                        visible = visible,
                        enter = expandHorizontally(
                            expandFrom = Alignment.Start,
                            animationSpec = tween(1000)
                        ),
                    ){
                        MetricCard(
                            value = 0,
                            label = "Total Time",
                            totalMillis = totalTime
                        )
                    }
                    AnimatedVisibility(
                        visible = visible,
                        enter = expandHorizontally(
                            expandFrom = Alignment.Start,
                            animationSpec = tween(1000)
                        ),
                    ) {
                        MetricCard(
                            value = totalExercises,
                            label = "Exercises"
                        )
                    }

                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    AnimatedVisibility(
                        visible = visible,
                        enter = expandHorizontally(
                            expandFrom = Alignment.Start,
                            animationSpec = tween(1000)
                        ),
                    )
                    {
                        MetricCard(
                            value = totalSets,
                            label = "Total Sets"
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Finish Button
            Button(
                onClick = onFinishClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Finish",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}


@Composable
private fun MetricCard(
    value: Int,
    label: String,
    totalMillis: Long? = null
) {
    Box(
        modifier = Modifier
            .width(120.dp)
            .background(color = MaterialTheme.colorScheme.surface)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                shape = RoundedCornerShape(12.dp)
            ),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (totalMillis != null) {
                AnimatedTimeCounter(
                    totalMillis = totalMillis,
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                )
            } else {
                AnimatedCounter(
                    targetValue = value,
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview
@Composable
private fun WorkoutCompletedPreview() {
    WorkoutCompletedScreen(
        totalTime = 3600000,
        totalExercises = 10,
        totalSets = 30,
        onFinishClick = {}
    )
}

@Composable
fun AnimatedCounter(
    targetValue: Int,
    modifier: Modifier = Modifier,
    style: TextStyle = TextStyle.Default,
    padLength: Int = 0,
    padChar: Char = '0'
) {
    val animatedValue = remember { Animatable(0f) }

    LaunchedEffect(targetValue) {
        animatedValue.animateTo(
            targetValue = targetValue.toFloat(),
            animationSpec = tween(
                durationMillis = 1000,
                easing = LinearEasing
            )
        )
    }

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Text(
            text = animatedValue.value.toInt().toString()
                .padStart(padLength, padChar),
            style = style
        )
    }
}

@Preview
@Composable
private fun MetricCounterAnimated() {
    AnimatedCounter(
        targetValue = 6,
        modifier = Modifier.padding(16.dp),
        style = MaterialTheme.typography.headlineLarge.copy(
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
    )
}

@Composable
fun AnimatedTimeCounter(
    totalMillis: Long,
    modifier: Modifier = Modifier,
    style: TextStyle = TextStyle.Default
) {
    val minutes = (totalMillis / 60000).toInt()
    val seconds = ((totalMillis % 60000) / 1000).toInt()
    var isInitial by remember { mutableStateOf(true) }

    LaunchedEffect(totalMillis) {
        if (isInitial) {
            // Animate from 0 to target values on first composition
            isInitial = false
        }
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        AnimatedCounter(
            targetValue = if (isInitial) 0 else minutes,
            style = style,
            padLength = 2
        )
        Text(text = ":", style = style)
        AnimatedCounter(
            targetValue = if (isInitial) 0 else seconds,
            style = style,
            padLength = 2
        )
    }
}

@Composable
fun ExpandHorizontally(modifier: Modifier = Modifier) {
    var visible by remember { mutableStateOf(false) }

    // Trigger visibility change after composition
    LaunchedEffect(Unit) {
        visible = true
    }

    AnimatedVisibility(
        visible = visible,
        enter = expandHorizontally(expandFrom = Alignment.Start, animationSpec = tween(1000)),
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .requiredHeight(200.dp)
        ) {
            Text("Hello, World!", modifier = modifier)
        }
    }
}

@Preview
@Composable
private fun ExpandHorizontallyPrev() {
    ExpandHorizontally()
}