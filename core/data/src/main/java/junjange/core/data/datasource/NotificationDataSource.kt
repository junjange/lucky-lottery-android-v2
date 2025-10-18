package junjange.core.data.datasource

interface NotificationDataSource {
    suspend fun postNotificationRegisterToken(
        deviceId: String,
        fcmToken: String,
    ): Result<Unit>
}
