package junjange.core.remote.model.response

import com.google.gson.annotations.SerializedName
import junjange.core.data.model.remote.LotteryGetContentEntity

data class LotteryGetContentResponse(
    val round: Int,
    val winningDate: String,
    @SerializedName("lotteryNumbersResponse") val lotteryGetNumbersResponse: List<LotteryGetNumbersResponse>,
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
