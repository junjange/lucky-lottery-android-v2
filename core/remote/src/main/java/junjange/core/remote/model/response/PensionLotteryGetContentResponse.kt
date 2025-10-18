package junjange.core.remote.model.response

import com.google.gson.annotations.SerializedName
import junjange.core.data.model.remote.PensionLotteryGetContentEntity

data class PensionLotteryGetContentResponse(
    val round: Int,
    val winningDate: String,
    val checkWinningBonus: Boolean,
    val pensionLotteryNumbersResponse: List<PensionLotteryNumbersResponse>,
    @SerializedName("winningLotteryNumbersResponse") val winningPensionLotteryNumbersResponse: WinningPensionLotteryNumbersResponse?,
    val winningPensionLotteryBonusNumbersResponse: WinningPensionLotteryBonusNumbersResponse?,
)

internal fun PensionLotteryGetContentResponse.toData() =
    PensionLotteryGetContentEntity(
        round = round,
        winningDate = winningDate,
        checkWinningBonus = checkWinningBonus,
        pensionLotteryNumbersEntity = pensionLotteryNumbersResponse.toData(),
        winningPensionLotteryNumbersEntity = winningPensionLotteryNumbersResponse?.toData(),
        winningPensionLotteryBonusNumbersEntity = winningPensionLotteryBonusNumbersResponse?.toData(),
    )
