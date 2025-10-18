package junjange.core.data.mapper

import junjange.core.data.model.remote.LotteryGetEntity
import junjange.core.domain.model.LotteryGet

internal fun LotteryGetEntity.toDomain() =
    LotteryGet(
        content = content.map { it.toDomain() },
        last = last,
    )
