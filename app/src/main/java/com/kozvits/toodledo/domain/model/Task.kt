package com.kozvits.toodledo.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Доменная модель задачи.
 * Содержит все поля, доступные в Toodledo API.
 */
@Parcelize
data class Task(
    val id: Long = 0L,
    val title: String = "",
    val note: String = "",
    val folderId: Long = 0L,
    val folderName: String = "",
    val contextId: Long = 0L,
    val contextName: String = "",
    val goalId: Long = 0L,
    val priority: Priority = Priority.NONE,
    val dueDate: Long = 0L,          // Unix timestamp, 0 = no date
    val dueTime: Long = 0L,          // Unix timestamp, 0 = no time
    val startDate: Long = 0L,
    val startTime: Long = 0L,
    val remind: Long = 0L,           // Minutes before due
    val repeat: RepeatType = RepeatType.NONE,
    val repeatFrom: Int = 0,         // 0=due date, 1=completion date
    val status: TaskStatus = TaskStatus.NONE,
    val star: Boolean = false,
    val isHot: Boolean = false,
    val completed: Long = 0L,        // Unix timestamp, 0 = not completed
    val length: Int = 0,             // Estimated minutes
    val timer: Int = 0,              // Timer seconds
    val added: Long = 0L,
    val modified: Long = 0L,
    val tag: String = "",
    val isDirty: Boolean = false     // Needs sync
) : Parcelable

enum class Priority(val value: Int, val label: String) {
    NEGATIVE(-1, "Negative"),
    NONE(0, "None"),
    LOW(1, "Low"),
    MEDIUM(2, "Medium"),
    HIGH(3, "High"),
    TOP(3, "Top");  // Toodledo uses 3 for top too

    companion object {
        fun fromValue(v: Int) = entries.firstOrNull { it.value == v } ?: NONE
    }
}

enum class TaskStatus(val value: Int, val label: String) {
    NONE(0, "None"),
    NEXT_ACTION(1, "Next Action"),
    ACTIVE(2, "Active"),
    PLANNING(3, "Planning"),
    DELEGATED(4, "Delegated"),
    WAITING(5, "Waiting"),
    HOLD(6, "Hold"),
    POSTPONED(7, "Postponed"),
    SOMEDAY(8, "Someday"),
    CANCELLED(9, "Cancelled"),
    REFERENCE(10, "Reference");

    companion object {
        fun fromValue(v: Int) = entries.firstOrNull { it.value == v } ?: NONE
    }
}

enum class RepeatType(val value: Int, val label: String) {
    NONE(0, "None"),
    WEEKLY(1, "Weekly"),
    MONTHLY(2, "Monthly"),
    YEARLY(4, "Yearly"),
    DAILY(5, "Daily"),
    BIWEEKLY(6, "Biweekly"),
    BIMONTHLY(7, "Bimonthly"),
    SEMIANNUALLY(8, "Semiannually"),
    QUARTERLY(9, "Quarterly");

    companion object {
        fun fromValue(v: Int) = entries.firstOrNull { it.value == v } ?: NONE
    }
}
