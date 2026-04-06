package com.kozvits.toodledo

import android.app.Application
import androidx.work.*
import com.kozvits.toodledo.data.repository.DatabaseSeeder
import com.kozvits.toodledo.domain.repository.SyncRepository
import com.kozvits.toodledo.presentation.sync.SyncWorker
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class ToodledoApp : Application(), Configuration.Provider {

    @Inject lateinit var seeder: DatabaseSeeder
    @Inject lateinit var syncRepository: SyncRepository

    private val appScope = CoroutineScope(Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()

        // Заполнить БД моковыми данными при первом запуске
        appScope.launch {
            seeder.seedIfEmpty()

            // Запустить периодическую синхронизацию согласно настройкам
            val settings = syncRepository.getSyncSettings()
            if (settings.autoSyncEnabled) {
                scheduleSyncWorker(settings.syncIntervalMinutes.toLong())
            }
        }
    }

    fun scheduleSyncWorker(intervalMinutes: Long) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val request = PeriodicWorkRequestBuilder<SyncWorker>(intervalMinutes, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 15, TimeUnit.MINUTES)
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            SyncWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.UPDATE,
            request
        )
    }

    fun cancelSyncWorker() {
        WorkManager.getInstance(this).cancelUniqueWork(SyncWorker.WORK_NAME)
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setMinimumLoggingLevel(android.util.Log.INFO)
            .build()
}
