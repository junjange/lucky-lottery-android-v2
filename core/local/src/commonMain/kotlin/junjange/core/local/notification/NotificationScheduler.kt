package junjange.core.local.notification

/**
 * Schedules recurring weekly lottery-result notifications.
 *
 * Android backs this with WorkManager + a system notification; iOS uses
 * UNUserNotificationCenter with a repeating calendar trigger.
 */
interface NotificationScheduler {
    fun scheduleLottoNotification()

    fun cancelLottoNotification()

    fun schedulePensionLottoNotification()

    fun cancelPensionLottoNotification()
}
