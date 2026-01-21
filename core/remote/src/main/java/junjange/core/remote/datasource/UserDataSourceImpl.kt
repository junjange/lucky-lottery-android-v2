package junjange.core.remote.datasource

import junjange.core.data.datasource.UserDataSource
import junjange.core.data.model.remote.UserMyInfoEntity
import junjange.core.remote.api.ApiService
import junjange.core.remote.model.request.NotificationRequest
import junjange.core.remote.model.request.UserMyInfoRequest
import junjange.core.remote.model.response.toData

internal class UserDataSourceImpl
    
    constructor(
        private val apiService: ApiService,
    ) : UserDataSource {
        override suspend fun patchLotteryNotification(notificationStatus: Boolean): Result<Unit> =
            runCatching {
                val body = NotificationRequest(notificationStatus = notificationStatus)
                apiService.patchLotteryNotification(
                    body = body,
                ).data
            }

        override suspend fun patchPensionLotteryNotification(notificationStatus: Boolean): Result<Unit> =
            runCatching {
                val body = NotificationRequest(notificationStatus = notificationStatus)
                apiService.patchPensionLotteryNotification(
                    body = body,
                ).data
            }

        override suspend fun patchUserMyInfo(
            profilePath: String?,
            nickname: String,
        ): Result<Unit> =
            runCatching {
                val body = UserMyInfoRequest(profilePath = profilePath, nickname = nickname)
                apiService.patchUserMyInfo(body = body).data
            }

        override suspend fun getUserMyInfo(): Result<UserMyInfoEntity> =
            runCatching {
                apiService.getUserMyIn().data.toData()
            }
    }
