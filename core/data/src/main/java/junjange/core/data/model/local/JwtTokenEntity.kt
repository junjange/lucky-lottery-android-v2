package junjange.core.data.model.local

data class JwtTokenEntity(
    val accessToken: String?,
    val refreshToken: String?,
)
