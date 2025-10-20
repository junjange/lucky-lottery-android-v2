package junjange.core.remote.model.response

import junjange.core.data.model.local.JwtTokenEntity

data class JwtTokenResponse(
    val accessToken: String,
    val refreshToken: String,
)

internal fun JwtTokenResponse.toData(): JwtTokenEntity =
    JwtTokenEntity(
        accessToken = accessToken,
        refreshToken = refreshToken,
    )
