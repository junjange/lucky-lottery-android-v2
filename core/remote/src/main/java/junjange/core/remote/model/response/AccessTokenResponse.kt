package junjange.core.remote.model.response

import junjange.core.data.model.remote.AccessTokenEntity

data class AccessTokenResponse(
    val accessToken: String,
)

fun AccessTokenResponse.toData() = AccessTokenEntity(accessToken = accessToken)
