package com.example.gymappsas

import android.app.NotificationManager
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowCompat
import com.example.gymappsas.services.TimerService
import com.example.gymappsas.ui.AppTheme
import com.example.gymappsas.util.NOTIFICATION_ID
import com.example.gymappsas.util.createWorkoutNotificationChannel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        createWorkoutNotificationChannel(this)
        enableEdgeToEdge()

        setContent {
            AppTheme(darkTheme = false, dynamicColor = false) {
                Navigation()
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        intent.let {
            if (it.getStringExtra("notification_action") == "stop_service") {
                val stopServiceIntent = Intent(this, TimerService::class.java)
                stopService(stopServiceIntent)

                val notificationManager =
                    getSystemService(NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.cancel(NOTIFICATION_ID)
            }
        }
    }

}