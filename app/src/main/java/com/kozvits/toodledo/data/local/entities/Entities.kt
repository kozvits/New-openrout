package com.kozvits.toodledo.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val title: String = "",
    val note: String = "",
    val folderId: Long = 0L,
    val folderName: String = "",
    val contextId: Long = 0L,
    val contextName: String = "",
    val goalId: Long = 0L,
    val priority: Int = 0,
    val dueDate: Long = 0L,
    val dueTime: Long = 0L,
    val startDate: Long = 0L,
    val startTime: Long = 0L,
    val remind: Long = 0L,
    val repeat: Int = 0,
    val repeatFrom: Int = 0,
    val status: Int = 0,
    val star: Boolean = false,
    val isHot: Boolean = false,
    val completed: Long = 0L,
    val length: Int = 0,
    val timer: Int = 0,
    val added: Long = 0L,
    val modified: Long = 0L,
    val tag: String = "",
    val isDirty: Boolean = false
)

@Entity(tableName = "folders")
data class FolderEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val name: String = "",
    val archived: Boolean = false,
    val isPrivate: Boolean = false,
    val order: Int = 0
)

@Entity(tableName = "contexts")
data class ContextEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val name: String = "",
    val order: Int = 0
)
