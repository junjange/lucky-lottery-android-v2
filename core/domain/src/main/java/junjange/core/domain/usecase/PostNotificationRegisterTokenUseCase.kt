package junjange.core.domain.usecase

import junjange.core.domain.repository.NotificationRepository
import javax.inject.Inject

class PostNotificationRegisterTokenUseCase
    @Inject
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
