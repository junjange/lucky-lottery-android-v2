package junjange.core.remote.model.response

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

import junjange.core.data.model.local.JwtTokenEntity

@Serializable
data class JwtTokenResponse(
    val accessToken: String,
    val refreshToken: String,
)

internal fun JwtTokenResponse.toData(): JwtTokenEntity =
    JwtTokenEntity(
        accessToken = accessToken,
        refreshToken = refreshToken,
    )
