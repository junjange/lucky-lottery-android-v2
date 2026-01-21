package junjange.core.remote.model.response

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

import junjange.core.data.model.remote.WinningPensionLotteryBonusNumbersEntity

@Serializable
data class WinningPensionLotteryBonusNumbersResponse(
    val bonusFirstNum: Int,
    val bonusSecondNum: Int,
    val bonusThirdNum: Int,
    val bonusFourthNum: Int,
    val bonusFifthNum: Int,
    val bonusSixthNum: Int,
)

internal fun WinningPensionLotteryBonusNumbersResponse.toData() =
    WinningPensionLotteryBonusNumbersEntity(
        bonusFirstNum = bonusFirstNum,
        bonusSecondNum = bonusSecondNum,
        bonusThirdNum = bonusThirdNum,
        bonusFourthNum = bonusFourthNum,
        bonusFifthNum = bonusFifthNum,
        bonusSixthNum = bonusSixthNum,
    )
