package com.example.tasks.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.tasks.worker.NotificationWorker
import timber.log.Timber

class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val request = OneTimeWorkRequestBuilder<NotificationWorker>().build()
        Timber.d(request.toString())
        WorkManager.getInstance(context).enqueue(request)
    }
}