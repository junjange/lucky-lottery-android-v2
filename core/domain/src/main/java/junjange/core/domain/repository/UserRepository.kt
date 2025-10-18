package junjange.core.domain.repository

import junjange.core.domain.model.UserMyInfo

interface UserRepository {
    suspend fun patchLotteryNotification(notificationStatus: Boolean): Result<Unit>

    suspend fun patchPensionLotteryNotification(notificationStatus: Boolean): Result<Unit>

    suspend fun patchUserMyInfo(
        profilePath: String?,
        nickname: String,
    ): Result<Unit>

    suspend fun getUserMyInfo(): Result<UserMyInfo>
}
