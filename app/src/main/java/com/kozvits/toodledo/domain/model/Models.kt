package com.kozvits.toodledo.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Folder(
    val id: Long = 0L,
    val name: String = "",
    val archived: Boolean = false,
    val isPrivate: Boolean = false,
    val order: Int = 0
) : Parcelable

@Parcelize
data class TaskContext(
    val id: Long = 0L,
    val name: String = "",
    val order: Int = 0
) : Parcelable

/**
 * Поля для сортировки задач и виджета горячего списка.
 */
enum class SortField(val key: String, val label: String) {
    PRIORITY("priority", "Priority"),
    DUE_DATE("dueDate", "Due Date"),
    FOLDER("folder", "Folder"),
    CONTEXT("context", "Context"),
    STATUS("status", "Status"),
    TITLE("title", "Title"),
    ADDED("added", "Date Added"),
    MODIFIED("modified", "Modified"),
    START_DATE("startDate", "Start Date");
}

enum class SortOrder { ASC, DESC }

data class SortConfig(
    val field: SortField,
    val order: SortOrder = SortOrder.ASC
)

/**
 * Настройки синхронизации с Toodledo.
 */
data class SyncSettings(
    val autoSyncEnabled: Boolean = true,
    val syncIntervalMinutes: Int = 30,   // 15, 30, 60, 180, 360, 720
    val lastSyncTime: Long = 0L,
    val apiLogin: String = "",
    val apiPassword: String = ""
)

/**
 * Состояние последней синхронизации.
 */
sealed class SyncState {
    object Idle : SyncState()
    object Running : SyncState()
    data class Success(val timestamp: Long) : SyncState()
    data class Error(val message: String) : SyncState()
}
