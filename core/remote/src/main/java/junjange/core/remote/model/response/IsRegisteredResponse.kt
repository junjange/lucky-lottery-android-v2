package junjange.core.remote.model.response

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

import junjange.core.data.model.remote.IsRegisteredEntity

@Serializable
data class IsRegisteredResponse(
    val isRegistered: Boolean,
)

fun IsRegisteredResponse.toData() = IsRegisteredEntity(isRegistered = isRegistered)
