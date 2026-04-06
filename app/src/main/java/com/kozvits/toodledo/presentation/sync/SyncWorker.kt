package com.kozvits.toodledo.presentation.sync

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.kozvits.toodledo.domain.repository.SyncRepository
import com.kozvits.toodledo.widget.HotListWidgetProvider
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val syncRepository: SyncRepository
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        return syncRepository.syncAll().fold(
            onSuccess = {
                // Обновить виджет после синхронизации
                HotListWidgetProvider.refresh(applicationContext)
                Result.success()
            },
            onFailure = { Result.retry() }
        )
    }

    companion object {
        const val WORK_NAME = "toodledo_periodic_sync"
    }
}
