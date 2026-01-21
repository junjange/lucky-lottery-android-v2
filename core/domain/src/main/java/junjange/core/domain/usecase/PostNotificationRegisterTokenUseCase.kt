package junjange.core.domain.usecase

import junjange.core.domain.repository.NotificationRepository

class PostNotificationRegisterTokenUseCase
    
    constructor(
        private val repository: NotificationRepository,
    ) {
        suspend operator fun invoke(
            deviceId: String,
            fcmToken: String,
        ): Result<Unit> =
            repository.postNotificationRegisterToken(
                deviceId = deviceId,
                fcmToken = fcmToken,
            )
    }
