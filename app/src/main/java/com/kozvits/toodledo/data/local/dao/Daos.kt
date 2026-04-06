package com.kozvits.toodledo.data.local.dao

import androidx.room.*
import com.kozvits.toodledo.data.local.entities.TaskEntity
import com.kozvits.toodledo.data.local.entities.FolderEntity
import com.kozvits.toodledo.data.local.entities.ContextEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Query("SELECT * FROM tasks WHERE completed = 0 ORDER BY priority DESC, dueDate ASC")
    fun observeActiveTasks(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE isHot = 1 AND completed = 0 ORDER BY priority DESC, dueDate ASC")
    fun observeHotList(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE isHot = 1 AND completed = 0 ORDER BY priority DESC, dueDate ASC")
    suspend fun getHotListSnapshot(): List<TaskEntity>

    @Query("SELECT * FROM tasks WHERE completed > 0 ORDER BY completed DESC")
    fun observeCompletedTasks(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): TaskEntity?

    @Query("SELECT * FROM tasks WHERE (title LIKE '%' || :q || '%' OR note LIKE '%' || :q || '%') AND completed = 0")
    fun searchTasks(q: String): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE folderId = :folderId AND completed = 0")
    fun observeByFolder(folderId: Long): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE contextId = :contextId AND completed = 0")
    fun observeByContext(contextId: Long): Flow<List<TaskEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: TaskEntity): Long

    @Update
    suspend fun update(task: TaskEntity)

    @Query("DELETE FROM tasks WHERE id = :id")
    suspend fun delete(id: Long)

    @Query("UPDATE tasks SET completed = :time, isDirty = 1 WHERE id = :id")
    suspend fun complete(id: Long, time: Long)

    @Query("UPDATE tasks SET isDirty = 0")
    suspend fun markAllClean()

    @Query("SELECT * FROM tasks WHERE isDirty = 1")
    suspend fun getDirtyTasks(): List<TaskEntity>
}

@Dao
interface FolderDao {
    @Query("SELECT * FROM folders ORDER BY `order` ASC")
    fun observeAll(): Flow<List<FolderEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(folder: FolderEntity): Long

    @Update
    suspend fun update(folder: FolderEntity)

    @Query("DELETE FROM folders WHERE id = :id")
    suspend fun delete(id: Long)

    @Query("SELECT * FROM folders")
    suspend fun getAll(): List<FolderEntity>
}

@Dao
interface ContextDao {
    @Query("SELECT * FROM contexts ORDER BY `order` ASC")
    fun observeAll(): Flow<List<ContextEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(context: ContextEntity): Long

    @Update
    suspend fun update(context: ContextEntity)

    @Query("DELETE FROM contexts WHERE id = :id")
    suspend fun delete(id: Long)

    @Query("SELECT * FROM contexts")
    suspend fun getAll(): List<ContextEntity>
}
