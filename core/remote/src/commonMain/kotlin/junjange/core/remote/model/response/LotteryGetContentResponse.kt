package junjange.core.remote.model.response

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

import junjange.core.data.model.remote.LotteryGetContentEntity

@Serializable
data class LotteryGetContentResponse(
    val round: Int,
    val winningDate: String,
    @SerialName("lotteryNumbersResponse") val lotteryGetNumbersResponse: List<LotteryGetNumbersResponse>,
    val winningLotteryNumbersResponse: WinningLotteryNumbersResponse?,
)

internal fun List<LotteryGetContentResponse>.toData() =
    map {
        LotteryGetContentEntity(
            round = it.round,
            winningDate = it.winningDate,
            lotteryGetNumbersEntity = it.lotteryGetNumbersResponse.toData(),
            winningLotteryNumbersEntity = it.winningLotteryNumbersResponse?.toData(),
        )
    }
