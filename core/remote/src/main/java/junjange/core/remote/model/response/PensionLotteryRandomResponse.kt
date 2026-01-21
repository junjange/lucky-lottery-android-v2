package junjange.core.remote.model.response

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

import junjange.core.data.model.remote.PensionLotteryRandomEntity

@Serializable
data class PensionLotteryRandomResponse(
    val pensionRound: Int,
    val pensionGroup: Int,
    val pensionFirstNum: Int,
    val pensionSecondNum: Int,
    val pensionThirdNum: Int,
    val pensionFourthNum: Int,
    val pensionFifthNum: Int,
    val pensionSixthNum: Int,
)

internal fun PensionLotteryRandomResponse.toData(): PensionLotteryRandomEntity =
    PensionLotteryRandomEntity(
        pensionRound = pensionRound,
        pensionGroup = pensionGroup,
        pensionFirstNum = pensionFirstNum,
        pensionSecondNum = pensionSecondNum,
        pensionThirdNum = pensionThirdNum,
        pensionFourthNum = pensionFourthNum,
        pensionFifthNum = pensionFifthNum,
        pensionSixthNum = pensionSixthNum,
    )
