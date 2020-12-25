package com.example.tasks.di

import android.content.Context
import androidx.room.Room
import com.example.tasks.data.db.ProjectDatabase
import com.example.tasks.data.network.ProjectAPI
import com.example.tasks.utils.Constants
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
}