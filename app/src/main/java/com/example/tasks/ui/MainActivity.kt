package com.example.tasks.ui

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import com.example.tasks.R
import com.example.tasks.databinding.ActivityMainBinding
import com.example.tasks.receivers.NotificationReceiver
import com.example.tasks.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setDefaultTheme()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        createNotificationChannel()
        startAlarm()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = Constants.NOTIFICATION_CHANNEL_NAME
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(Constants.NOTIFICATION_CHANNEL_ID, name, importance)
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    private fun setDefaultTheme() {
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val mode = prefs.getString("theme", getString(R.string.pref_theme_default_value))!!
        val values = arrayOf(
            AppCompatDelegate.MODE_NIGHT_NO,
            AppCompatDelegate.MODE_NIGHT_YES,
            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        )
        AppCompatDelegate.setDefaultNightMode(values[mode.toInt()])
    }

    private fun startAlarm() {
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, NotificationReceiver::class.java)
        val alarmIntent = PendingIntent.getBroadcast(
            applicationContext, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT
        )
        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 9)
        }
        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            alarmIntent
        )
    }
}