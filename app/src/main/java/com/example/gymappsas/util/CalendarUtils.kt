package com.example.gymappsas.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import com.kizitonwose.calendar.compose.CalendarLayoutInfo
import com.kizitonwose.calendar.compose.CalendarState
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.Week
import com.kizitonwose.calendar.core.yearMonth
import kotlinx.coroutines.flow.filterNotNull
import java.time.DayOfWeek
import java.time.Month
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

@Composable
    fun rememberFirstMostVisibleMonth(
        state: CalendarState,
        viewportPercent: Float = 50f,
    ): CalendarMonth {
        val visibleMonth = remember(state) { mutableStateOf(state.firstVisibleMonth) }
        LaunchedEffect(state) {
            snapshotFlow { state.layoutInfo.firstMostVisibleMonth(viewportPercent) }
                .filterNotNull()
                .collect { month -> visibleMonth.value = month }
        }
        return visibleMonth.value
    }

fun YearMonth.displayText(short: Boolean = false): String {
    return "${this.month.displayText(short = short)} ${this.year}"
}

fun Month.displayText(short: Boolean = true): String {
    val style = if (short) TextStyle.SHORT else TextStyle.FULL
    return getDisplayName(style, Locale.ENGLISH)
}

fun DayOfWeek.displayText(uppercase: Boolean = false): String {
    return getDisplayName(TextStyle.SHORT, Locale.ENGLISH).let { value ->
        if (uppercase) value.uppercase(Locale.ENGLISH) else value
    }
}

fun getWeekPageTitle(week: Week): String {
    val firstDate = week.days.first().date
    val lastDate = week.days.last().date
    return when {
        firstDate.yearMonth == lastDate.yearMonth -> {
            firstDate.yearMonth.displayText()
        }
        firstDate.year == lastDate.year -> {
            "${firstDate.month.displayText(short = false)} - ${lastDate.yearMonth.displayText()}"
        }
        else -> {
            "${firstDate.yearMonth.displayText()} - ${lastDate.yearMonth.displayText()}"
        }
    }
}


    private fun CalendarLayoutInfo.firstMostVisibleMonth(viewportPercent: Float = 50f): CalendarMonth? {
        return if (visibleMonthsInfo.isEmpty()) {
            null
        } else {
            val viewportSize = (viewportEndOffset + viewportStartOffset) * viewportPercent / 100f
            visibleMonthsInfo.firstOrNull { itemInfo ->
                if (itemInfo.offset < 0) {
                    itemInfo.offset + itemInfo.size >= viewportSize
                } else {
                    itemInfo.size - itemInfo.offset >= viewportSize
                }
            }?.month
        }
    }
