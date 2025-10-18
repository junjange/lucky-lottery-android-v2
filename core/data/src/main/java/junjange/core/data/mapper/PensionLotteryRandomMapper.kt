package junjange.core.data.mapper

import junjange.core.data.model.remote.PensionLotteryRandomEntity
import junjange.core.domain.model.PensionLotteryRandom

fun PensionLotteryRandomEntity.toDomain() =
    PensionLotteryRandom(
        pensionRound = pensionRound,
        pensionGroup = pensionGroup,
        pensionFirstNum = pensionFirstNum,
        pensionSecondNum = pensionSecondNum,
        pensionThirdNum = pensionThirdNum,
        pensionFourthNum = pensionFourthNum,
        pensionFifthNum = pensionFifthNum,
        pensionSixthNum = pensionSixthNum,
    )
