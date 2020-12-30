package com.example.tasks.data.db.dao

import androidx.room.*
import com.example.tasks.data.db.entities.Project
import com.example.tasks.data.db.entities.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface ProjectDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProject(project: Project)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProjects(projects: List<Project>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTasks(tasks: List<Task>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task)

    @Query("SELECT * FROM project")
    fun getProjects(): Flow<List<Project>>

    @Query("SELECT * FROM task WHERE completed = 0")
    fun getPendingTasks(): List<Task>

    @Query("SELECT * FROM task WHERE project = :project")
    fun getTasks(project: String): Flow<List<Task>>

    @Query("DELETE from project")
    suspend fun deleteProjects()

    @Query("DELETE from task")
    suspend fun deleteTasks()

    @Query("DELETE from task WHERE project = :id")
    suspend fun deleteProjectTasks(id: String)

    @Transaction
    suspend fun deleteProjectAndTasks(project: Project) {
        deleteProject(project)
        deleteProjectTasks(project.id)
    }

    @Delete
    suspend fun deleteTask(task: Task)

    @Delete
    suspend fun deleteProject(project: Project)
}