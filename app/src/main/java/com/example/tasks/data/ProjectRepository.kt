package com.example.tasks.data

import com.example.tasks.data.db.dao.ProjectDao
import com.example.tasks.data.db.entities.Project
import com.example.tasks.data.db.entities.Task
import javax.inject.Inject

class ProjectRepository @Inject constructor(private val projectDao: ProjectDao) {

    suspend fun insertProject(project: Project) = projectDao.insertProject(project)

    suspend fun insertTask(task: Task) = projectDao.insertTask(task)

    suspend fun getProjects() = projectDao.getProjects()

    suspend fun getTasks(project: String) = projectDao.getTasks(project)
}