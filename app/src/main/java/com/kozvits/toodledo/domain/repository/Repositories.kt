package com.kozvits.toodledo.domain.repository

import com.kozvits.toodledo.domain.model.*
import kotlinx.coroutines.flow.Flow

/**
 * Интерфейс репозитория задач.
 * Используется в use case'ах — реализация в data-слое.
 */
interface TaskRepository {

    /** Поток всех задач (не завершённых) */
    fun observeTasks(): Flow<List<Task>>

    /** Поток горячего списка */
    fun observeHotList(): Flow<List<Task>>

    /** Поток завершённых задач */
    fun observeCompletedTasks(): Flow<List<Task>>

    /** Получить задачу по ID */
    suspend fun getTaskById(id: Long): Task?

    /** Добавить задачу */
    suspend fun addTask(task: Task): Long

    /** Обновить задачу */
    suspend fun updateTask(task: Task)

    /** Удалить задачу */
    suspend fun deleteTask(id: Long)

    /** Отметить задачу выполненной */
    suspend fun completeTask(id: Long, completionTime: Long = System.currentTimeMillis() / 1000)

    /** Поиск задач по строке */
    fun searchTasks(query: String): Flow<List<Task>>

    /** Задачи по папке */
    fun observeTasksByFolder(folderId: Long): Flow<List<Task>>

    /** Задачи по контексту */
    fun observeTasksByContext(contextId: Long): Flow<List<Task>>
}

/**
 * Интерфейс репозитория папок.
 */
interface FolderRepository {
    fun observeFolders(): Flow<List<Folder>>
    suspend fun addFolder(folder: Folder): Long
    suspend fun updateFolder(folder: Folder)
    suspend fun deleteFolder(id: Long)
}

/**
 * Интерфейс репозитория контекстов.
 */
interface ContextRepository {
    fun observeContexts(): Flow<List<TaskContext>>
    suspend fun addContext(context: TaskContext): Long
    suspend fun updateContext(context: TaskContext)
    suspend fun deleteContext(id: Long)
}

/**
 * Интерфейс репозитория синхронизации.
 */
interface SyncRepository {
    suspend fun syncAll(): Result<Unit>
    suspend fun getSyncSettings(): SyncSettings
    suspend fun saveSyncSettings(settings: SyncSettings)
    fun observeSyncState(): Flow<SyncState>
}
