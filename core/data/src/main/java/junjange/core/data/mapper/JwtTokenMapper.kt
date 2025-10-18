package junjange.core.data.mapper

import junjange.core.data.model.local.JwtTokenEntity
import junjange.core.domain.model.JwtToken

internal fun JwtTokenEntity.toDomain(): JwtToken {
    return JwtToken(
        accessToken = this.accessToken,
        refreshToken = this.refreshToken,
    )
}

internal fun JwtToken.toData(): JwtTokenEntity {
    return JwtTokenEntity(
        accessToken = this.accessToken,
        refreshToken = this.refreshToken,
    )
}
