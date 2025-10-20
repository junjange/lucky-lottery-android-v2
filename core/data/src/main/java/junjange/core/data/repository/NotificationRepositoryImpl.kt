package junjange.core.data.repository

import junjange.core.data.datasource.NotificationDataSource
import junjange.core.domain.repository.NotificationRepository
import javax.inject.Inject

internal class NotificationRepositoryImpl
    @Inject
    constructor(
        private val dataSource: NotificationDataSource,
    ) : NotificationRepository {
        override suspend fun postNotificationRegisterToken(
            deviceId: String,
            fcmToken: String,
        ): Result<Unit> =
            runCatching {
                dataSource.postNotificationRegisterToken(deviceId = deviceId, fcmToken = fcmToken)
            }
    }
