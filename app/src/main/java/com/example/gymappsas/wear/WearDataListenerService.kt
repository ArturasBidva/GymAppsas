package com.example.gymappsas.wear

import android.util.Log
import com.example.gymappsas.data.db.dao.StepsDao
import com.example.gymappsas.data.db.models.steps.StepsEntity
import com.google.android.gms.wearable.DataEvent
import com.google.android.gms.wearable.DataEventBuffer
import com.google.android.gms.wearable.DataMapItem
import com.google.android.gms.wearable.WearableListenerService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class WearDataListenerService : WearableListenerService() {

    @Inject
    lateinit var stepsDao: StepsDao

    override fun onDataChanged(dataEvents: DataEventBuffer) {
        super.onDataChanged(dataEvents)

        Log.d("WearSync", "onDataChanged called with ${dataEvents.count} events")

        for (event in dataEvents) {
            Log.d("WearSync", "Event type: ${event.type}, path: ${event.dataItem.uri.path}")

            if (event.type == DataEvent.TYPE_CHANGED) {
                val item = event.dataItem
                if (item.uri.path?.compareTo("/steps_data") == 0) {
                    val dataMap = DataMapItem.fromDataItem(item).dataMap
                    val stepCount = dataMap.getInt("step_count")
                    val dateFromWatch = dataMap.getString("date") ?: ""
                    val timestamp = dataMap.getLong("timestamp")

                    Log.d(
                        "WearSync",
                        "✓ Received steps: $stepCount on '$dateFromWatch' at $timestamp"
                    )
                    Log.d("WearSync", "Current date: ${getCurrentDate()}")

                    // Save to Room database
                    saveStepData(stepCount, dateFromWatch, timestamp)
                }
            }
        }
    }

    private fun saveStepData(steps: Int, dateFromWatch: String, timestamp: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val phoneDate = getCurrentDate()
                val stepsEntity = StepsEntity(
                    date = phoneDate, // Always use the phone's date
                    steps = steps,
                    timestamp = timestamp
                )
                stepsDao.upsertSteps(stepsEntity)
                Log.d(
                    "WearSync",
                    "Steps saved to database: $steps for date $phoneDate (watch reported $dateFromWatch)"
                )
            } catch (e: Exception) {
                Log.e("WearSync", "Error saving steps to database", e)
            }
        }
    }

    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(Date())
    }
}