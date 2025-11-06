package junjange.core.domain.usecase

import junjange.core.domain.model.LuckyLotteryNotification
import junjange.core.domain.repository.UserRepository
import javax.inject.Inject

class GetNotificationUseCase
    @Inject
    constructor(
        private val repository: UserRepository,
    ) {
        suspend operator fun invoke(): Result<LuckyLotteryNotification> = repository.getNotification()
    }
