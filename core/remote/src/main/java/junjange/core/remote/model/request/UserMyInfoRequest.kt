package junjange.core.remote.model.request

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class UserMyInfoRequest(
    val profilePath: String?,
    val nickname: String,
)
