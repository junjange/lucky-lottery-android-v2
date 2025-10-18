package junjange.core.domain.usecase

import junjange.core.domain.repository.UserRepository
import javax.inject.Inject

class PatchPensionLotteryNotificationUseCase
    @Inject
    constructor(
        private val repository: UserRepository,
    ) {
        suspend operator fun invoke(notificationStatus: Boolean): Result<Unit> =
            repository.patchPensionLotteryNotification(notificationStatus = notificationStatus)
    }
