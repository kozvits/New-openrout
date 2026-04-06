package com.kozvits.toodledo.di

import android.content.Context
import androidx.room.Room
import com.kozvits.toodledo.data.local.ToodledoDatabase
import com.kozvits.toodledo.data.local.dao.ContextDao
import com.kozvits.toodledo.data.local.dao.FolderDao
import com.kozvits.toodledo.data.local.dao.TaskDao
import com.kozvits.toodledo.data.repository.*
import com.kozvits.toodledo.domain.repository.*
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): ToodledoDatabase =
        Room.databaseBuilder(context, ToodledoDatabase::class.java, "toodledo.db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides fun provideTaskDao(db: ToodledoDatabase): TaskDao = db.taskDao()
    @Provides fun provideFolderDao(db: ToodledoDatabase): FolderDao = db.folderDao()
    @Provides fun provideContextDao(db: ToodledoDatabase): ContextDao = db.contextDao()
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds @Singleton
    abstract fun bindTaskRepository(impl: TaskRepositoryImpl): TaskRepository

    @Binds @Singleton
    abstract fun bindFolderRepository(impl: FolderRepositoryImpl): FolderRepository

    @Binds @Singleton
    abstract fun bindContextRepository(impl: ContextRepositoryImpl): ContextRepository

    @Binds @Singleton
    abstract fun bindSyncRepository(impl: SyncRepositoryImpl): SyncRepository
}
