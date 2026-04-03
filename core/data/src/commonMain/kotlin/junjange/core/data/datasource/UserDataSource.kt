package junjange.core.data.datasource

import junjange.core.data.model.remote.UserMyInfoEntity

interface UserDataSource {
    suspend fun patchLotteryNotification(notificationStatus: Boolean): Result<Unit>

    suspend fun patchPensionLotteryNotification(notificationStatus: Boolean): Result<Unit>

    suspend fun patchUserMyInfo(
        profilePath: String?,
        nickname: String,
    ): Result<Unit>

    suspend fun getUserMyInfo(): Result<UserMyInfoEntity>
}
