package com.kozvits.toodledo.data.mock

import com.kozvits.toodledo.domain.model.*
import java.util.concurrent.TimeUnit

/**
 * Моковые данные для разработки и тестирования.
 * Заменяется реальным ToodledoRemoteDataSource при подключении к API.
 */
object MockDataSource {

    private val now = System.currentTimeMillis() / 1000L
    private fun daysFromNow(days: Int) = now + TimeUnit.DAYS.toSeconds(days.toLong())
    private fun daysAgo(days: Int) = now - TimeUnit.DAYS.toSeconds(days.toLong())

    val folders = listOf(
        Folder(id = 1L, name = "Work", archived = false, order = 0),
        Folder(id = 2L, name = "Personal", archived = false, order = 1),
        Folder(id = 3L, name = "Shopping", archived = false, order = 2),
        Folder(id = 4L, name = "Projects", archived = false, order = 3),
        Folder(id = 5L, name = "Archive", archived = true, order = 4),
    )

    val contexts = listOf(
        TaskContext(id = 1L, name = "@Home", order = 0),
        TaskContext(id = 2L, name = "@Work", order = 1),
        TaskContext(id = 3L, name = "@Phone", order = 2),
        TaskContext(id = 4L, name = "@Errands", order = 3),
        TaskContext(id = 5L, name = "@Computer", order = 4),
    )

    val tasks = listOf(
        Task(
            id = 1L, title = "Prepare quarterly report",
            note = "Include data from all departments. Focus on Q4 metrics.",
            folderId = 1L, folderName = "Work",
            contextId = 5L, contextName = "@Computer",
            priority = Priority.TOP, dueDate = daysFromNow(2),
            status = TaskStatus.NEXT_ACTION, isHot = true,
            star = true, added = daysAgo(5), modified = daysAgo(1),
            tag = "report, quarterly", isDirty = false
        ),
        Task(
            id = 2L, title = "Call insurance company",
            note = "Policy number: ABC-12345. Ask about claim status.",
            folderId = 2L, folderName = "Personal",
            contextId = 3L, contextName = "@Phone",
            priority = Priority.HIGH, dueDate = daysFromNow(1),
            status = TaskStatus.NEXT_ACTION, isHot = true,
            added = daysAgo(3), modified = daysAgo(3), isDirty = false
        ),
        Task(
            id = 3L, title = "Buy groceries",
            note = "Milk, eggs, bread, butter, coffee, vegetables.",
            folderId = 3L, folderName = "Shopping",
            contextId = 4L, contextName = "@Errands",
            priority = Priority.MEDIUM, dueDate = daysFromNow(0),
            status = TaskStatus.ACTIVE, isHot = false,
            added = daysAgo(1), modified = daysAgo(1), isDirty = false
        ),
        Task(
            id = 4L, title = "Review project proposal",
            note = "Team submitted the new mobile app proposal. Need feedback by Friday.",
            folderId = 4L, folderName = "Projects",
            contextId = 5L, contextName = "@Computer",
            priority = Priority.HIGH, dueDate = daysFromNow(3),
            status = TaskStatus.ACTIVE, isHot = true,
            star = true, added = daysAgo(2), modified = daysAgo(2), isDirty = false
        ),
        Task(
            id = 5L, title = "Fix kitchen faucet",
            note = "Dripping faucet in the kitchen. Need a wrench and new seals.",
            folderId = 2L, folderName = "Personal",
            contextId = 1L, contextName = "@Home",
            priority = Priority.MEDIUM, dueDate = daysFromNow(7),
            status = TaskStatus.WAITING, isHot = false,
            added = daysAgo(10), modified = daysAgo(5), isDirty = false
        ),
        Task(
            id = 6L, title = "Schedule dentist appointment",
            folderId = 2L, folderName = "Personal",
            contextId = 3L, contextName = "@Phone",
            priority = Priority.LOW, dueDate = daysFromNow(14),
            status = TaskStatus.SOMEDAY, isHot = false,
            added = daysAgo(20), modified = daysAgo(20), isDirty = false
        ),
        Task(
            id = 7L, title = "Update team on project status",
            note = "Send weekly status email to stakeholders.",
            folderId = 1L, folderName = "Work",
            contextId = 2L, contextName = "@Work",
            priority = Priority.HIGH, dueDate = daysFromNow(0),
            status = TaskStatus.NEXT_ACTION, isHot = true,
            repeat = RepeatType.WEEKLY, added = daysAgo(30), modified = daysAgo(7), isDirty = false
        ),
        Task(
            id = 8L, title = "Read 'Clean Architecture' book",
            note = "Chapter 5 onwards. Make notes.",
            folderId = 2L, folderName = "Personal",
            contextId = 1L, contextName = "@Home",
            priority = Priority.NONE, dueDate = 0L,
            status = TaskStatus.ACTIVE, isHot = false,
            added = daysAgo(14), modified = daysAgo(14), isDirty = false
        ),
        Task(
            id = 9L, title = "Backup laptop data",
            folderId = 2L, folderName = "Personal",
            contextId = 5L, contextName = "@Computer",
            priority = Priority.MEDIUM, dueDate = daysFromNow(5),
            status = TaskStatus.PLANNING, isHot = false,
            added = daysAgo(7), modified = daysAgo(7), isDirty = false
        ),
        Task(
            id = 10L, title = "Prepare presentation slides",
            note = "Board meeting next week. 15 slides max.",
            folderId = 1L, folderName = "Work",
            contextId = 5L, contextName = "@Computer",
            priority = Priority.TOP, dueDate = daysFromNow(4),
            status = TaskStatus.ACTIVE, isHot = true,
            star = true, added = daysAgo(1), modified = daysAgo(1), isDirty = false
        ),
        // Completed tasks
        Task(
            id = 11L, title = "Submit tax return",
            folderId = 2L, folderName = "Personal",
            priority = Priority.TOP,
            completed = daysAgo(2), added = daysAgo(30), modified = daysAgo(2)
        ),
        Task(
            id = 12L, title = "Team retrospective meeting",
            folderId = 1L, folderName = "Work",
            contextId = 2L, contextName = "@Work",
            priority = Priority.HIGH,
            completed = daysAgo(5), added = daysAgo(7), modified = daysAgo(5)
        ),
    )
}
