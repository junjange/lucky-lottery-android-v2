package junjange.core.domain.usecase

import junjange.core.domain.repository.UserRepository

class PatchPensionLotteryNotificationUseCase
    
    constructor(
        private val repository: UserRepository,
    ) {
        suspend operator fun invoke(notificationStatus: Boolean): Result<Unit> =
            repository.patchPensionLotteryNotification(notificationStatus = notificationStatus)
    }
