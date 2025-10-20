package junjange.core.data.mapper

import junjange.core.data.model.remote.GoogleOauthTokenEntity
import junjange.core.domain.model.GoogleOauthToken

internal fun GoogleOauthTokenEntity.toDomain(): GoogleOauthToken {
    return GoogleOauthToken(
        accessToken = accessToken,
        expiresIn = expiresIn,
        scope = scope,
        tokenType = tokenType,
        idToken = idToken,
    )
}
