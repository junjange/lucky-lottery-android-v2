package junjange.core.data.mapper

import junjange.core.data.model.local.KakaoAccessTokenEntity
import junjange.core.domain.model.KakaoAccessToken

internal fun KakaoAccessTokenEntity.toDomain(): KakaoAccessToken {
    return KakaoAccessToken(
        idToken = this.idToken,
        accessToken = this.accessToken,
    )
}
