package junjange.core.remote.model.response

import junjange.core.data.model.remote.IsRegisteredEntity

data class IsRegisteredResponse(
    val isRegistered: Boolean,
)

fun IsRegisteredResponse.toData() = IsRegisteredEntity(isRegistered = isRegistered)
