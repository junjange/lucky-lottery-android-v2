package junjange.core.domain.usecase

import junjange.core.domain.repository.UserRepository
import javax.inject.Inject

class PatchLotteryNotificationUseCase
    @Inject
    constructor(
        private val repository: UserRepository,
    ) {
        suspend operator fun invoke(notificationStatus: Boolean): Result<Unit> =
            repository.patchLotteryNotification(notificationStatus = notificationStatus)
    }
