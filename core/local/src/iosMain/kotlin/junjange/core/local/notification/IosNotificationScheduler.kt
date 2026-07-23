package junjange.core.local.notification

import platform.Foundation.NSDateComponents
import platform.UserNotifications.UNAuthorizationOptionAlert
import platform.UserNotifications.UNAuthorizationOptionSound
import platform.UserNotifications.UNCalendarNotificationTrigger
import platform.UserNotifications.UNMutableNotificationContent
import platform.UserNotifications.UNNotificationRequest
import platform.UserNotifications.UNNotificationSound
import platform.UserNotifications.UNUserNotificationCenter

internal class IosNotificationScheduler : NotificationScheduler {
    override fun scheduleLottoNotification() =
        schedule(
            id = LOTTO_ID,
            weekday = SATURDAY,
            hour = LOTTO_NOTIFICATION_HOUR,
            title = "로또6/45 당첨번호가 발표되었어요!",
            body = "지금 바로 당첨번호를 확인해보세요",
        )

    override fun cancelLottoNotification() = cancel(LOTTO_ID)

    override fun schedulePensionLottoNotification() =
        schedule(
            id = PENSION_LOTTO_ID,
            weekday = THURSDAY,
            hour = PENSION_LOTTO_NOTIFICATION_HOUR,
            title = "연금복권720+ 당첨번호가 발표되었어요!",
            body = "지금 바로 당첨번호를 확인해보세요",
        )

    override fun cancelPensionLottoNotification() = cancel(PENSION_LOTTO_ID)

    private fun schedule(
        id: String,
        weekday: Long,
        hour: Long,
        title: String,
        body: String,
    ) {
        val center = UNUserNotificationCenter.currentNotificationCenter()
        center.requestAuthorizationWithOptions(
            UNAuthorizationOptionAlert or UNAuthorizationOptionSound,
        ) { _, _ -> }

        val content =
            UNMutableNotificationContent().apply {
                setTitle(title)
                setBody(body)
                setSound(UNNotificationSound.defaultSound())
            }

        val components =
            NSDateComponents().apply {
                setWeekday(weekday)
                setHour(hour)
                setMinute(0)
            }
        val trigger =
            UNCalendarNotificationTrigger.triggerWithDateMatchingComponents(components, repeats = true)
        val request = UNNotificationRequest.requestWithIdentifier(id, content, trigger)
        center.addNotificationRequest(request, null)
    }

    private fun cancel(id: String) {
        UNUserNotificationCenter.currentNotificationCenter()
            .removePendingNotificationRequestsWithIdentifiers(listOf(id))
    }

    private companion object {
        const val LOTTO_ID = "lotto_notification_work"
        const val PENSION_LOTTO_ID = "pension_lotto_notification_work"

        // NSDateComponents weekday: 1 = Sunday … 7 = Saturday
        const val SATURDAY = 7L
        const val THURSDAY = 5L
        const val LOTTO_NOTIFICATION_HOUR = 21L
        const val PENSION_LOTTO_NOTIFICATION_HOUR = 20L
    }
}
