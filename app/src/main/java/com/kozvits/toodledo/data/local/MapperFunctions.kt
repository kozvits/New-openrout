package com.kozvits.toodledo.data.local

import com.kozvits.toodledo.data.local.entities.ContextEntity
import com.kozvits.toodledo.data.local.entities.FolderEntity
import com.kozvits.toodledo.data.local.entities.TaskEntity
import com.kozvits.toodledo.domain.model.*

fun toDomain(e: TaskEntity) = Task(
    id = e.id, title = e.title, note = e.note,
    folderId = e.folderId, folderName = e.folderName,
    contextId = e.contextId, contextName = e.contextName,
    goalId = e.goalId,
    priority = Priority.fromValue(e.priority),
    dueDate = e.dueDate, dueTime = e.dueTime,
    startDate = e.startDate, startTime = e.startTime,
    remind = e.remind,
    repeat = RepeatType.fromValue(e.repeat),
    repeatFrom = e.repeatFrom,
    status = TaskStatus.fromValue(e.status),
    star = e.star, isHot = e.isHot,
    completed = e.completed,
    length = e.length, timer = e.timer,
    added = e.added, modified = e.modified,
    tag = e.tag, isDirty = e.isDirty
)

fun toEntity(t: Task) = TaskEntity(
    id = t.id, title = t.title, note = t.note,
    folderId = t.folderId, folderName = t.folderName,
    contextId = t.contextId, contextName = t.contextName,
    goalId = t.goalId,
    priority = t.priority.value,
    dueDate = t.dueDate, dueTime = t.dueTime,
    startDate = t.startDate, startTime = t.startTime,
    remind = t.remind,
    repeat = t.repeat.value,
    repeatFrom = t.repeatFrom,
    status = t.status.value,
    star = t.star, isHot = t.isHot,
    completed = t.completed,
    length = t.length, timer = t.timer,
    added = t.added, modified = t.modified,
    tag = t.tag, isDirty = t.isDirty
)

fun toDomain(e: FolderEntity) = Folder(id = e.id, name = e.name, archived = e.archived, isPrivate = e.isPrivate, order = e.order)
fun toEntity(f: Folder) = FolderEntity(id = f.id, name = f.name, archived = f.archived, isPrivate = f.isPrivate, order = f.order)

fun toDomain(e: ContextEntity) = TaskContext(id = e.id, name = e.name, order = e.order)
fun toEntity(c: TaskContext) = ContextEntity(id = c.id, name = c.name, order = c.order)
