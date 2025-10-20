package junjange.core.data.mapper

import junjange.core.data.model.remote.LotteryGetNumbersEntity
import junjange.core.domain.model.LotteryGetNumbers

internal fun List<LotteryGetNumbersEntity>.toDomain() =
    map {
        LotteryGetNumbers(
            id = it.id,
            firstNum = it.firstNum,
            secondNum = it.secondNum,
            thirdNum = it.thirdNum,
            fourthNum = it.fourthNum,
            fifthNum = it.fifthNum,
            sixthNum = it.sixthNum,
            correctNumbers = it.correctNumbers,
            rank = it.rank,
        )
    }
