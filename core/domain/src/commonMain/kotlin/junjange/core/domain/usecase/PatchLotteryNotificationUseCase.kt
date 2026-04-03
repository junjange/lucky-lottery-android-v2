package junjange.core.domain.usecase

import junjange.core.domain.repository.UserRepository

class PatchLotteryNotificationUseCase
    
    constructor(
        private val repository: UserRepository,
    ) {
        suspend operator fun invoke(notificationStatus: Boolean): Result<Unit> =
            repository.patchLotteryNotification(notificationStatus = notificationStatus)
    }
