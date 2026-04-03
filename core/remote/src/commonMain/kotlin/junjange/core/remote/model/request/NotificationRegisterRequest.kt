package junjange.core.remote.model.request

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class NotificationRegisterRequest(
    val deviceId: String,
    val fcmToken: String,
)
