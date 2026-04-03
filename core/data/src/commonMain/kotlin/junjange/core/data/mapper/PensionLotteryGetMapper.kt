package junjange.core.data.mapper

import junjange.core.data.model.remote.PensionLotteryGetEntity
import junjange.core.domain.model.PensionLotteryGet

internal fun PensionLotteryGetEntity.toDomain() =
    PensionLotteryGet(
        content = content.map { it.toDomain() },
        last = last,
    )
