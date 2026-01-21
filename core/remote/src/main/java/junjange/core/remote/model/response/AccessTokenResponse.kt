package junjange.core.remote.model.response

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

import junjange.core.data.model.remote.AccessTokenEntity

@Serializable
data class AccessTokenResponse(
    val accessToken: String,
)

fun AccessTokenResponse.toData() = AccessTokenEntity(accessToken = accessToken)
