package com.example.tasks.data.repositories

import com.example.tasks.data.db.dao.ProjectDao
import com.example.tasks.data.db.entities.Project
import com.example.tasks.data.db.entities.Task
import com.example.tasks.data.network.ProjectAPI
import com.example.tasks.data.network.payloads.CreateProjectPayload
import com.example.tasks.data.network.payloads.CreateTaskPayload
import com.example.tasks.data.network.payloads.UpdateTaskPayload
import javax.inject.Inject

class ProjectRepository @Inject constructor(
    private val projectDao: ProjectDao,
    private val projectAPI: ProjectAPI,
) {
    suspend fun insertProject(project: Project) = projectDao.insertProject(project)

    suspend fun insertProjects(projects: List<Project>) = projectDao.insertProjects(projects)

    suspend fun insertTask(task: Task) = projectDao.insertTask(task)

    suspend fun insertTasks(tasks: List<Task>) = projectDao.insertTasks(tasks)

    fun getProjects() = projectDao.getProjects()

    fun getTasks(project: String) = projectDao.getTasks(project)

    suspend fun deleteProject(id: String) = projectAPI.deleteProject(id)

    suspend fun deleteProjects() = projectDao.deleteProjects()

    suspend fun deleteTasks() = projectDao.deleteTasks()

    suspend fun deleteProjectAndTasks(project: Project) = projectDao.deleteProjectAndTasks(project)

    suspend fun deleteTask(task: Task) = projectDao.deleteTask(task)

    suspend fun fetchProjects() = projectAPI.getProjects()

    suspend fun fetchTasks(id: String) = projectAPI.getTasks(id)

    suspend fun createProject(payload: CreateProjectPayload) = projectAPI.createProject(payload)

    suspend fun createTask(payload: CreateTaskPayload) = projectAPI.createTask(payload)

    suspend fun deleteTask(id: String) = projectAPI.deleteTask(id)

    suspend fun updateTask(
        id: String, payload: UpdateTaskPayload,
    ) = projectAPI.updateTask(id, payload)
}