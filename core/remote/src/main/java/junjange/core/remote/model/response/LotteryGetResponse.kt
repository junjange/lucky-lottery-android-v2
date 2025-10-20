package junjange.core.remote.model.response

import junjange.core.data.model.remote.LotteryGetEntity

data class LotteryGetResponse(
    val content: List<LotteryGetContentResponse>,
    val last: Boolean,
)

internal fun LotteryGetResponse.toData() =
    LotteryGetEntity(
        content = content.toData(),
        last = last,
    )
