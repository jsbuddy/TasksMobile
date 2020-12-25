package com.example.tasks.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.tasks.data.db.dao.ProjectDao
import com.example.tasks.data.db.entities.Project
import com.example.tasks.data.db.entities.Task
import com.example.tasks.utils.Constants

@Database(
    entities = [Project::class, Task::class],
    version = 2,
    exportSchema = false
)
abstract class ProjectDatabase : RoomDatabase() {
    abstract fun projectDao(): ProjectDao

    companion object {
        private fun migration() = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE project ADD COLUMN deadline TEXT NOT NULL DEFAULT ''")
            }
        }

        @Volatile
        private var instance: ProjectDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also { instance = it }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                ProjectDatabase::class.java,
                Constants.DATABASE_NAME
            ).addMigrations(migration()).build()
    }
}