package junjange.core.local.notification

import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import junjange.core.local.worker.LottoNotificationWorker
import junjange.core.local.worker.PensionLottoNotificationWorker
import java.util.Calendar
import java.util.concurrent.TimeUnit

internal class AndroidNotificationScheduler(
    private val workManager: WorkManager,
) : NotificationScheduler {
    override fun scheduleLottoNotification() {
        val initialDelayMillis = calculateInitialDelay(Calendar.SATURDAY, LOTTO_NOTIFICATION_HOUR)
        val workRequest =
            PeriodicWorkRequestBuilder<LottoNotificationWorker>(7, TimeUnit.DAYS)
                .setInitialDelay(initialDelayMillis, TimeUnit.MILLISECONDS)
                .build()
        workManager.enqueueUniquePeriodicWork(
            LOTTO_WORK_NAME,
            ExistingPeriodicWorkPolicy.REPLACE,
            workRequest,
        )
    }

    override fun cancelLottoNotification() {
        workManager.cancelUniqueWork(LOTTO_WORK_NAME)
    }

    override fun schedulePensionLottoNotification() {
        val initialDelayMillis = calculateInitialDelay(Calendar.THURSDAY, PENSION_LOTTO_NOTIFICATION_HOUR)
        val workRequest =
            PeriodicWorkRequestBuilder<PensionLottoNotificationWorker>(7, TimeUnit.DAYS)
                .setInitialDelay(initialDelayMillis, TimeUnit.MILLISECONDS)
                .build()
        workManager.enqueueUniquePeriodicWork(
            PENSION_LOTTO_WORK_NAME,
            ExistingPeriodicWorkPolicy.REPLACE,
            workRequest,
        )
    }

    override fun cancelPensionLottoNotification() {
        workManager.cancelUniqueWork(PENSION_LOTTO_WORK_NAME)
    }

    private fun calculateInitialDelay(
        targetDayOfWeek: Int,
        targetHour: Int,
    ): Long {
        val now = Calendar.getInstance()
        val currentDayOfWeek = now.get(Calendar.DAY_OF_WEEK)

        var daysUntilTarget = targetDayOfWeek - currentDayOfWeek
        if (daysUntilTarget < 0) {
            daysUntilTarget += 7
        } else if (daysUntilTarget == 0) {
            val currentHour = now.get(Calendar.HOUR_OF_DAY)
            val currentMinute = now.get(Calendar.MINUTE)
            if (currentHour > targetHour || (currentHour == targetHour && currentMinute > 0)) {
                daysUntilTarget = 7
            }
        }

        val target = Calendar.getInstance()
        target.add(Calendar.DAY_OF_YEAR, daysUntilTarget)
        target.set(Calendar.HOUR_OF_DAY, targetHour)
        target.set(Calendar.MINUTE, 0)
        target.set(Calendar.SECOND, 0)
        target.set(Calendar.MILLISECOND, 0)

        return target.timeInMillis - now.timeInMillis
    }

    private companion object {
        const val LOTTO_WORK_NAME = "lotto_notification_work"
        const val PENSION_LOTTO_WORK_NAME = "pension_lotto_notification_work"
        const val LOTTO_NOTIFICATION_HOUR = 21
        const val PENSION_LOTTO_NOTIFICATION_HOUR = 20
    }
}
