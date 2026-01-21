package junjange.core.domain.usecase

import junjange.core.domain.model.LuckyLotteryNotification
import junjange.core.domain.repository.UserRepository

class GetNotificationUseCase
    
    constructor(
        private val repository: UserRepository,
    ) {
        suspend operator fun invoke(): Result<LuckyLotteryNotification> = repository.getNotification()
    }
