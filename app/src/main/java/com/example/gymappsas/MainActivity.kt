package com.example.gymappsas

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.gymappsas.databinding.ActivityMainBinding
import com.example.gymappsas.services.TimerService
import com.example.gymappsas.util.NOTIFICATION_ID
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
//    private lateinit var fab: FloatingActionButton
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createNotificationChannel()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        fab = findViewById(R.id.fab)
//        val navHostFragment = supportFragmentManager
//            .findFragmentById(R.id.my_nav) as NavHostFragment
//        val navController = navHostFragment.navController
//
//
//        navController.addOnDestinationChangedListener { _, destination, _ ->
//            when (destination.id) {
//                R.id.workoutSchedule -> fab.show()
//                else -> fab.hide()
//            }
//        }
    }
    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            "TIMER_CHANNEL",
            "Timer Notifications",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Channel for timer notifications"
        }
        // Register the channel with the system; you can't change the importance
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager?.createNotificationChannel(channel)
    }
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        intent.let {
            if (it.getStringExtra("notification_action") == "stop_service") {

                val stopServiceIntent = Intent(this, TimerService::class.java)
                stopService(stopServiceIntent)

                val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.cancel(NOTIFICATION_ID)
            }
        }
    }

}