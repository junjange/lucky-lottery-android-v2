package junjange.core.data.repository

import junjange.core.data.datasource.NotificationLocalDataSource
import junjange.core.data.datasource.UserDataSource
import junjange.core.data.mapper.toDomain
import junjange.core.domain.model.LuckyLotteryNotification
import junjange.core.domain.model.UserMyInfo
import junjange.core.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl
    @Inject
    constructor(
        private val dataSource: UserDataSource,
        private val notificationLocalDataSource: NotificationLocalDataSource,
    ) : UserRepository {
        override suspend fun patchLotteryNotification(notificationStatus: Boolean): Result<Unit> =
            notificationLocalDataSource.saveLotteryNotification(isEnabled = notificationStatus)

        override suspend fun patchPensionLotteryNotification(notificationStatus: Boolean): Result<Unit> =
            notificationLocalDataSource.savePensionLotteryNotification(isEnabled = notificationStatus)

        override suspend fun getNotification(): Result<LuckyLotteryNotification> =
            notificationLocalDataSource.getNotification().mapCatching { it.toDomain() }

        override suspend fun patchUserMyInfo(
            profilePath: String?,
            nickname: String,
        ): Result<Unit> =
            runCatching {
                dataSource.patchUserMyInfo(profilePath = profilePath, nickname = nickname)
            }

        override suspend fun getUserMyInfo(): Result<UserMyInfo> = dataSource.getUserMyInfo().mapCatching { it.toDomain() }
    }
