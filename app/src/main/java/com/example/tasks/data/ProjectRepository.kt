package com.example.tasks.data

import com.example.tasks.data.db.dao.ProjectDao
import com.example.tasks.data.db.entities.Project
import com.example.tasks.data.db.entities.Task
import com.example.tasks.data.network.ProjectAPI
import com.example.tasks.data.network.payloads.CreateProjectPayload
import javax.inject.Inject

class ProjectRepository @Inject constructor(
    private val projectDao: ProjectDao,
    private val projectAPI: ProjectAPI
) {
    suspend fun insertProject(project: Project) = projectDao.insertProject(project)

    suspend fun insertProjects(projects: List<Project>) = projectDao.insertProjects(projects)

    suspend fun insertTask(task: Task) = projectDao.insertTask(task)

    fun loadProjects() = projectDao.getProjects()

    fun getTasks(project: String) = projectDao.getTasks(project)

    suspend fun deleteProjects() = projectDao.deleteProjects()

    suspend fun fetchProjects() = projectAPI.getProjects()

    suspend fun createProject(name: CreateProjectPayload) = projectAPI.createProject(name)
}