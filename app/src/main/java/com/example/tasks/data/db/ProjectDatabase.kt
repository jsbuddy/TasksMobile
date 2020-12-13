package com.example.tasks.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.tasks.data.db.dao.ProjectDao
import com.example.tasks.data.db.entities.Project
import com.example.tasks.data.db.entities.Task

@Database(
    entities = [Project::class, Task::class],
    version = 1
)
abstract class ProjectDatabase : RoomDatabase() {
    abstract fun projectDao(): ProjectDao

    companion object {
        @Volatile
        private var instance: ProjectDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also { instance = it }
        }

        private fun createDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext, ProjectDatabase::class.java, "projects.db"
        ).build()
    }
}