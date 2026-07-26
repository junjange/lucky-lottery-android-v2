package junjange.core.remote.model.error

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName


@Serializable
data class ErrorResponseImpl(
    @SerialName("success") override val success: Boolean,
    @SerialName("status") override val status: Int,
    @SerialName("reason") override val reason: String,
    @SerialName("timeStamp") override val timeStamp: String,
    @SerialName("path") override val path: String,
) : ErrorResponse
