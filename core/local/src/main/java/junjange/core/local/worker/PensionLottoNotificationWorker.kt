package junjange.core.local.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import junjange.core.notification.LottoNotificationManager
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class PensionLottoNotificationWorker(
    context: Context,
    params: WorkerParameters,
) : CoroutineWorker(context, params), KoinComponent {
    private val notificationManager: LottoNotificationManager by inject()
        override suspend fun doWork(): Result {
            return try {
                notificationManager.sendPensionLottoNotification()
                Result.success()
            } catch (e: Exception) {
                Result.failure()
            }
        }
    }