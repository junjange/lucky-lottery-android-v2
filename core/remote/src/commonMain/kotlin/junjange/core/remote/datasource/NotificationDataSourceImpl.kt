package junjange.core.remote.datasource

import junjange.core.data.datasource.NotificationDataSource
import junjange.core.remote.api.ApiService
import junjange.core.remote.model.request.NotificationRegisterRequest

internal class NotificationDataSourceImpl
    
    constructor(
        private val apiService: ApiService,
    ) : NotificationDataSource {
        override suspend fun postNotificationRegisterToken(
            deviceId: String,
            fcmToken: String,
        ): Result<Unit> =
            runCatching {
                val body = NotificationRegisterRequest(deviceId = deviceId, fcmToken = fcmToken)
                apiService.postNotificationRegisterToken(body = body).data
            }
    }
