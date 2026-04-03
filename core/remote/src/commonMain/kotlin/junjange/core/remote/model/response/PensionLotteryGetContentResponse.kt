package junjange.core.remote.model.response

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

import junjange.core.data.model.remote.PensionLotteryGetContentEntity

@Serializable
data class PensionLotteryGetContentResponse(
    val round: Int,
    val winningDate: String,
    val checkWinningBonus: Boolean,
    val pensionLotteryNumbersResponse: List<PensionLotteryNumbersResponse>,
    @SerialName("winningLotteryNumbersResponse") val winningPensionLotteryNumbersResponse: WinningPensionLotteryNumbersResponse?,
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
