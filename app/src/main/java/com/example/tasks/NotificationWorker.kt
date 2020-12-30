package com.example.tasks

import android.content.Context
import androidx.hilt.Assisted
import androidx.hilt.work.WorkerInject
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.tasks.data.repositories.ProjectRepository
import com.example.tasks.utils.TaskNotification
import com.example.tasks.utils.Utils

class NotificationWorker @WorkerInject constructor(
    @Assisted val context: Context,
    @Assisted workerParams: WorkerParameters,
    private val repository: ProjectRepository,
) : Worker(context, workerParams) {

    override fun doWork(): Result {
        repository.getPendingTasks().let {
            val tasks = it.filter { task -> Utils.isToday(task.due) }
            TaskNotification.notify(
                context, "Due tasks", "You have ${tasks.size} task(s) due today, tap to view"
            )
        }
        return Result.success()
    }
}