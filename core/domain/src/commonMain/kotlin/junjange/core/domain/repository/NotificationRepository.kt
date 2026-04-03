package junjange.core.domain.repository

interface NotificationRepository {
    suspend fun postNotificationRegisterToken(
        deviceId: String,
        fcmToken: String,
    ): Result<Unit>
}
