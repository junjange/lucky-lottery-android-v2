package junjange.core.remote.model.response

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

import junjange.core.data.model.remote.LotteryGetEntity

@Serializable
data class LotteryGetResponse(
    val content: List<LotteryGetContentResponse>,
    val last: Boolean,
)

internal fun LotteryGetResponse.toData() =
    LotteryGetEntity(
        content = content.toData(),
        last = last,
    )
