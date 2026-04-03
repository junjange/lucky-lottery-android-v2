package junjange.core.data.mapper

import junjange.core.data.model.remote.IsRegisteredEntity
import junjange.core.domain.model.IsRegistered

internal fun IsRegisteredEntity.toDomain(): IsRegistered = IsRegistered(isRegistered = this.isRegistered)
