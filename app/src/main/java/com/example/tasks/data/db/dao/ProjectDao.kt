package com.example.tasks.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tasks.data.db.entities.Project
import com.example.tasks.data.db.entities.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface ProjectDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProject(project: Project)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task)

    @Query("SELECT * FROM project")
    suspend fun getProjects(): Flow<List<Project>>

    @Query("SELECT * FROM project")
    suspend fun getTasks(project: String): Flow<List<Task>>
}