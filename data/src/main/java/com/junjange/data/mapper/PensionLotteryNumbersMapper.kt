package com.junjange.data.mapper

import com.junjange.data.model.remote.PensionLotteryNumbersEntity
import com.junjange.domain.model.PensionLotteryNumbers

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
