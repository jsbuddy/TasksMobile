package com.example.tasks.di

import android.content.Context
import com.example.tasks.data.db.ProjectDatabase
import com.example.tasks.data.db.dao.ProjectDao
import com.example.tasks.data.network.ProjectAPI
import com.example.tasks.data.repositories.ProjectRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideProjectDatabase(@ApplicationContext context: Context) = ProjectDatabase(context)

    @Singleton
    @Provides
    fun provideProjectDao(db: ProjectDatabase) = db.projectDao()

    @Singleton
    @Provides
    fun provideProjectApi(): ProjectAPI = ProjectAPI()

    @Singleton
    @Provides
    fun provideRepository(
        projectDao: ProjectDao, projectAPI: ProjectAPI,
    ) = ProjectRepository(projectDao, projectAPI)
}