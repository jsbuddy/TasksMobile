package com.example.tasks.di

import android.content.Context
import com.example.tasks.data.AuthHelpers
import com.example.tasks.data.db.ProjectDatabase
import com.example.tasks.data.db.dao.ProjectDao
import com.example.tasks.data.network.AuthAPI
import com.example.tasks.data.network.ProjectAPI
import com.example.tasks.data.network.interceptors.ProjectApiInterceptor
import com.example.tasks.data.repositories.AuthRepository
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
    fun provideProjectApi(projectApiInterceptor: ProjectApiInterceptor): ProjectAPI =
        ProjectAPI(projectApiInterceptor)

    @Singleton
    @Provides
    fun provideAuthApi(): AuthAPI = AuthAPI()

    @Singleton
    @Provides
    fun provideRepository(
        projectDao: ProjectDao, projectAPI: ProjectAPI,
    ) = ProjectRepository(projectDao, projectAPI)

    @Singleton
    @Provides
    fun provideAuthRepository(
        authAPI: AuthAPI, authHelpers: AuthHelpers, projectApiInterceptor: ProjectApiInterceptor
    ) = AuthRepository(authAPI, authHelpers, projectApiInterceptor)

    @Singleton
    @Provides
    fun provideAuthenticator(@ApplicationContext context: Context) = AuthHelpers(context)

    @Singleton
    @Provides
    fun provideProjectApiInterceptor() = ProjectApiInterceptor()
}