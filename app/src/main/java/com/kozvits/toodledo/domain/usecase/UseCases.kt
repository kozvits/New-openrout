package com.kozvits.toodledo.domain.usecase

import com.kozvits.toodledo.domain.model.*
import com.kozvits.toodledo.domain.repository.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

// ─── Task Use Cases ───────────────────────────────────────────────────────────

class GetTasksUseCase @Inject constructor(private val repo: TaskRepository) {
    operator fun invoke(): Flow<List<Task>> = repo.observeTasks()
}

class GetHotListUseCase @Inject constructor(private val repo: TaskRepository) {
    operator fun invoke(): Flow<List<Task>> = repo.observeHotList()
}

class GetCompletedTasksUseCase @Inject constructor(private val repo: TaskRepository) {
    operator fun invoke(): Flow<List<Task>> = repo.observeCompletedTasks()
}

class GetTaskByIdUseCase @Inject constructor(private val repo: TaskRepository) {
    suspend operator fun invoke(id: Long): Task? = repo.getTaskById(id)
}

class AddTaskUseCase @Inject constructor(private val repo: TaskRepository) {
    suspend operator fun invoke(task: Task): Long = repo.addTask(task)
}

class UpdateTaskUseCase @Inject constructor(private val repo: TaskRepository) {
    suspend operator fun invoke(task: Task) = repo.updateTask(task)
}

class DeleteTaskUseCase @Inject constructor(private val repo: TaskRepository) {
    suspend operator fun invoke(id: Long) = repo.deleteTask(id)
}

class CompleteTaskUseCase @Inject constructor(private val repo: TaskRepository) {
    suspend operator fun invoke(id: Long) = repo.completeTask(id)
}

class SearchTasksUseCase @Inject constructor(private val repo: TaskRepository) {
    operator fun invoke(query: String): Flow<List<Task>> = repo.searchTasks(query)
}

class GetTasksByFolderUseCase @Inject constructor(private val repo: TaskRepository) {
    operator fun invoke(folderId: Long): Flow<List<Task>> = repo.observeTasksByFolder(folderId)
}

class GetTasksByContextUseCase @Inject constructor(private val repo: TaskRepository) {
    operator fun invoke(contextId: Long): Flow<List<Task>> = repo.observeTasksByContext(contextId)
}

// ─── Folder Use Cases ─────────────────────────────────────────────────────────

class GetFoldersUseCase @Inject constructor(private val repo: FolderRepository) {
    operator fun invoke(): Flow<List<Folder>> = repo.observeFolders()
}

class AddFolderUseCase @Inject constructor(private val repo: FolderRepository) {
    suspend operator fun invoke(folder: Folder): Long = repo.addFolder(folder)
}

class DeleteFolderUseCase @Inject constructor(private val repo: FolderRepository) {
    suspend operator fun invoke(id: Long) = repo.deleteFolder(id)
}

// ─── Context Use Cases ────────────────────────────────────────────────────────

class GetContextsUseCase @Inject constructor(private val repo: ContextRepository) {
    operator fun invoke(): Flow<List<TaskContext>> = repo.observeContexts()
}

class AddContextUseCase @Inject constructor(private val repo: ContextRepository) {
    suspend operator fun invoke(context: TaskContext): Long = repo.addContext(context)
}

// ─── Sync Use Cases ───────────────────────────────────────────────────────────

class SyncNowUseCase @Inject constructor(private val repo: SyncRepository) {
    suspend operator fun invoke(): Result<Unit> = repo.syncAll()
}

class GetSyncSettingsUseCase @Inject constructor(private val repo: SyncRepository) {
    suspend operator fun invoke(): SyncSettings = repo.getSyncSettings()
}

class SaveSyncSettingsUseCase @Inject constructor(private val repo: SyncRepository) {
    suspend operator fun invoke(settings: SyncSettings) = repo.saveSyncSettings(settings)
}

class ObserveSyncStateUseCase @Inject constructor(private val repo: SyncRepository) {
    operator fun invoke(): Flow<SyncState> = repo.observeSyncState()
}

// ─── Hot List Sort Use Case ───────────────────────────────────────────────────

class GetSortedHotListUseCase @Inject constructor(private val repo: TaskRepository) {
    operator fun invoke(
        sort1: SortConfig,
        sort2: SortConfig?,
        sort3: SortConfig?
    ): Flow<List<Task>> = repo.observeHotList().map { tasks ->
        tasks.sortedWith(buildComparator(sort1, sort2, sort3))
    }

    private fun buildComparator(
        s1: SortConfig, s2: SortConfig?, s3: SortConfig?
    ): Comparator<Task> {
        var cmp = fieldComparator(s1)
        if (s2 != null) cmp = cmp.then(fieldComparator(s2))
        if (s3 != null) cmp = cmp.then(fieldComparator(s3))
        return cmp
    }

    private fun fieldComparator(cfg: SortConfig): Comparator<Task> {
        val base: Comparator<Task> = when (cfg.field) {
            SortField.PRIORITY  -> compareByDescending { it.priority.value }
            SortField.DUE_DATE  -> compareBy { it.dueDate }
            SortField.TITLE     -> compareBy { it.title.lowercase() }
            SortField.FOLDER    -> compareBy { it.folderName.lowercase() }
            SortField.CONTEXT   -> compareBy { it.contextName.lowercase() }
            SortField.STATUS    -> compareBy { it.status.value }
            SortField.ADDED     -> compareBy { it.added }
            SortField.MODIFIED  -> compareBy { it.modified }
            SortField.START_DATE -> compareBy { it.startDate }
        }
        return if (cfg.order == SortOrder.DESC) Comparator { a, b -> base.compare(b, a) }
        else base
    }
}
