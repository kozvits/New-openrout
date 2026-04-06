package com.kozvits.toodledo.data.repository

import com.kozvits.toodledo.data.local.*
import com.kozvits.toodledo.data.local.dao.ContextDao
import com.kozvits.toodledo.data.local.dao.FolderDao
import com.kozvits.toodledo.data.local.dao.TaskDao
import com.kozvits.toodledo.data.mock.MockDataSource
import com.kozvits.toodledo.domain.model.*
import com.kozvits.toodledo.domain.repository.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepositoryImpl @Inject constructor(private val taskDao: TaskDao) : TaskRepository {
    override fun observeTasks() = taskDao.observeActiveTasks().map { it.map { e -> toDomain(e) } }
    override fun observeHotList() = taskDao.observeHotList().map { it.map { e -> toDomain(e) } }
    override fun observeCompletedTasks() = taskDao.observeCompletedTasks().map { it.map { e -> toDomain(e) } }
    override suspend fun getTaskById(id: Long) = taskDao.getById(id)?.let { toDomain(it) }
    override suspend fun addTask(task: Task): Long {
        val now = System.currentTimeMillis() / 1000L
        return taskDao.insert(toEntity(task.copy(added = now, modified = now, isDirty = true)))
    }
    override suspend fun updateTask(task: Task) {
        taskDao.update(toEntity(task.copy(modified = System.currentTimeMillis() / 1000L, isDirty = true)))
    }
    override suspend fun deleteTask(id: Long) = taskDao.delete(id)
    override suspend fun completeTask(id: Long, completionTime: Long) = taskDao.complete(id, completionTime)
    override fun searchTasks(query: String) = taskDao.searchTasks(query).map { it.map { e -> toDomain(e) } }
    override fun observeTasksByFolder(folderId: Long) = taskDao.observeByFolder(folderId).map { it.map { e -> toDomain(e) } }
    override fun observeTasksByContext(contextId: Long) = taskDao.observeByContext(contextId).map { it.map { e -> toDomain(e) } }
}

@Singleton
class FolderRepositoryImpl @Inject constructor(private val folderDao: FolderDao) : FolderRepository {
    override fun observeFolders() = folderDao.observeAll().map { it.map { e -> toDomain(e) } }
    override suspend fun addFolder(folder: Folder) = folderDao.insert(toEntity(folder))
    override suspend fun updateFolder(folder: Folder) = folderDao.update(toEntity(folder))
    override suspend fun deleteFolder(id: Long) = folderDao.delete(id)
}

@Singleton
class ContextRepositoryImpl @Inject constructor(private val contextDao: ContextDao) : ContextRepository {
    override fun observeContexts() = contextDao.observeAll().map { it.map { e -> toDomain(e) } }
    override suspend fun addContext(context: TaskContext) = contextDao.insert(toEntity(context))
    override suspend fun updateContext(context: TaskContext) = contextDao.update(toEntity(context))
    override suspend fun deleteContext(id: Long) = contextDao.delete(id)
}

@Singleton
class SyncRepositoryImpl @Inject constructor(
    private val taskDao: TaskDao,
    private val folderDao: FolderDao,
    private val contextDao: ContextDao
) : SyncRepository {
    private val _state = MutableStateFlow<SyncState>(SyncState.Idle)
    private var _settings = SyncSettings(autoSyncEnabled = true, syncIntervalMinutes = 30)

    override fun observeSyncState(): Flow<SyncState> = _state.asStateFlow()

    override suspend fun syncAll(): Result<Unit> = runCatching {
        _state.value = SyncState.Running
        delay(1500L) // Заглушка — здесь будет реальный Toodledo API call
        _settings = _settings.copy(lastSyncTime = System.currentTimeMillis() / 1000L)
        _state.value = SyncState.Success(_settings.lastSyncTime)
    }.onFailure { _state.value = SyncState.Error(it.message ?: "Sync failed") }

    override suspend fun getSyncSettings() = _settings
    override suspend fun saveSyncSettings(settings: SyncSettings) { _settings = settings }
}

@Singleton
class DatabaseSeeder @Inject constructor(
    private val taskDao: TaskDao,
    private val folderDao: FolderDao,
    private val contextDao: ContextDao
) {
    suspend fun seedIfEmpty() {
        if (folderDao.getAll().isEmpty()) {
            MockDataSource.folders.forEach { folderDao.insert(toEntity(it)) }
            MockDataSource.contexts.forEach { contextDao.insert(toEntity(it)) }
            MockDataSource.tasks.forEach { taskDao.insert(toEntity(it)) }
        }
    }
}
