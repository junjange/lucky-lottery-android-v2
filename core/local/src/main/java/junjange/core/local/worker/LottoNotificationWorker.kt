package junjange.core.local.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import junjange.core.notification.LottoNotificationManager

@HiltWorker
class LottoNotificationWorker
    @AssistedInject
    constructor(
        @Assisted context: Context,
        @Assisted params: WorkerParameters,
        private val notificationManager: LottoNotificationManager,
    ) : CoroutineWorker(context, params) {
        override suspend fun doWork(): Result {
            return try {
                notificationManager.sendLottoNotification()
                Result.success()
            } catch (e: Exception) {
                Result.failure()
            }
        }
    }