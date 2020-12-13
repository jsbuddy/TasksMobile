package com.example.tasks.di

import android.content.Context
import androidx.room.Room
import com.example.tasks.data.db.ProjectDatabase
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
    fun provideProjectDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, ProjectDatabase::class.java, "projects.db").build()

    @Singleton
    @Provides
    fun provideProjectDao(db: ProjectDatabase) = db.projectDao()
}