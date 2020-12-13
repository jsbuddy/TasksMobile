package com.example.tasks.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.tasks.data.db.dao.ProjectDao
import com.example.tasks.data.db.entities.Project
import com.example.tasks.data.db.entities.Task

@Database(
    entities = [Project::class, Task::class],
    version = 1,
    exportSchema = false
)
abstract class ProjectDatabase : RoomDatabase() {
    abstract fun projectDao(): ProjectDao
}