package com.kozvits.toodledo.util

import java.text.SimpleDateFormat
import java.util.*

private val dateFormat = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())
private val dateTimeFormat = SimpleDateFormat("MMM d, yyyy HH:mm", Locale.getDefault())

/** Unix timestamp (seconds) → readable date string */
fun formatDate(unixSeconds: Long): String {
    if (unixSeconds <= 0L) return ""
    return dateFormat.format(Date(unixSeconds * 1000L))
}

/** Unix timestamp (seconds) → readable date+time string */
fun formatDateTime(unixSeconds: Long): String {
    if (unixSeconds <= 0L) return ""
    return dateTimeFormat.format(Date(unixSeconds * 1000L))
}

/** Check if a unix-second timestamp is today */
fun isToday(unixSeconds: Long): Boolean {
    if (unixSeconds <= 0L) return false
    val taskCal = Calendar.getInstance().apply { time = Date(unixSeconds * 1000L) }
    val now = Calendar.getInstance()
    return taskCal.get(Calendar.YEAR) == now.get(Calendar.YEAR) &&
            taskCal.get(Calendar.DAY_OF_YEAR) == now.get(Calendar.DAY_OF_YEAR)
}

/** Check if a unix-second timestamp is in the past */
fun isOverdue(unixSeconds: Long): Boolean {
    if (unixSeconds <= 0L) return false
    return unixSeconds < System.currentTimeMillis() / 1000L
}
