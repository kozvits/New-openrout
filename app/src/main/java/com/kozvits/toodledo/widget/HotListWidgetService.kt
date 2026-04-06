package com.kozvits.toodledo.widget

import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.kozvits.toodledo.R
import com.kozvits.toodledo.data.local.ToodledoDatabase
import com.kozvits.toodledo.data.local.toDomain
import com.kozvits.toodledo.domain.model.Priority
import com.kozvits.toodledo.domain.model.Task
import com.kozvits.toodledo.util.formatDate
import kotlinx.coroutines.runBlocking
import android.graphics.Color

/**
 * RemoteViewsService предоставляет данные для ListView в виджете.
 */
class HotListWidgetService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory =
        HotListFactory(applicationContext)
}

class HotListFactory(private val context: Context) : RemoteViewsService.RemoteViewsFactory {

    private var tasks: List<Task> = emptyList()

    override fun onCreate() { load() }
    override fun onDataSetChanged() { load() }
    override fun onDestroy() {}

    private fun load() {
        runBlocking {
            try {
                val db = ToodledoDatabase.getInstance(context)
                val entities = db.taskDao().getHotListSnapshot()
                tasks = entities
                    .map { toDomain(it) }
                    .sortedWith(compareByDescending<Task> { it.priority.value }.thenBy { it.dueDate })
                    .take(10)
            } catch (e: Exception) {
                tasks = emptyList()
            }
        }
    }

    override fun getCount() = tasks.size
    override fun getViewTypeCount() = 1
    override fun hasStableIds() = true
    override fun getItemId(pos: Int) = tasks[pos].id

    override fun getViewAt(pos: Int): RemoteViews {
        val task = tasks.getOrNull(pos) ?: return RemoteViews(context.packageName, R.layout.item_widget_task)
        val rv = RemoteViews(context.packageName, R.layout.item_widget_task)

        rv.setTextViewText(R.id.widget_task_title, task.title)
        rv.setTextViewText(R.id.widget_task_due,
            if (task.dueDate > 0) formatDate(task.dueDate) else "")

        val priorityColor = when (task.priority) {
            Priority.TOP, Priority.HIGH -> Color.parseColor("#FF5252")
            Priority.MEDIUM -> Color.parseColor("#FFC107")
            Priority.LOW -> Color.parseColor("#4CAF50")
            else -> Color.parseColor("#455A64")
        }
        rv.setInt(R.id.widget_priority_bar, "setBackgroundColor", priorityColor)

        return rv
    }

    override fun getLoadingView(): RemoteViews? = null
}
