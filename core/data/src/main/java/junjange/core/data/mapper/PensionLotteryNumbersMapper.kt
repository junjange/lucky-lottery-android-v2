package junjange.core.data.mapper

import junjange.core.data.model.remote.PensionLotteryNumbersEntity
import junjange.core.domain.model.PensionLotteryNumbers

internal fun List<PensionLotteryNumbersEntity>.toDomain() =
    map {
        PensionLotteryNumbers(
            id = it.id,
            group = it.pensionGroup,
            firstNum = it.pensionFirstNum,
            secondNum = it.pensionSecondNum,
            thirdNum = it.pensionThirdNum,
            fourthNum = it.pensionFourthNum,
            fifthNum = it.pensionFifthNum,
            sixthNum = it.pensionSixthNum,
            rank = it.rank,
            checkWinningBonus = it.checkWinningBonus,
            correctNumbers = it.correctNumbers,
            bonusCorrectNumbers = it.bonusCorrectNumbers,
        )
    }
