package junjange.core.remote.model.response

import junjange.core.data.model.remote.PensionLotteryGetEntity

data class PensionLotteryGetResponse(
    val content: List<PensionLotteryGetContentResponse>,
    val last: Boolean,
)

internal fun PensionLotteryGetResponse.toData(): PensionLotteryGetEntity =
    PensionLotteryGetEntity(
        content = content.map { it.toData() },
        last = last,
    )
