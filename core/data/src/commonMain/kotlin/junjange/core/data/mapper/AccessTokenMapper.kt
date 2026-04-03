package junjange.core.data.mapper

import junjange.core.data.model.remote.AccessTokenEntity
import junjange.core.domain.model.AccessToken

internal fun AccessTokenEntity.toDomain(): AccessToken = AccessToken(accessToken = this.accessToken)
