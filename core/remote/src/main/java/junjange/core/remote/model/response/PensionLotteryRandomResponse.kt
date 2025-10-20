package junjange.core.remote.model.response

import junjange.core.data.model.remote.PensionLotteryRandomEntity

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
