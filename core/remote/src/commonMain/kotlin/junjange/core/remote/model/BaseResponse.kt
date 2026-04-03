package junjange.core.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class BaseResponse<T>(
    val status: Int,
    val data: T,
    val success: Boolean,
    val timeStamp: String,
)
