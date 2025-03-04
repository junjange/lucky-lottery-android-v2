package com.junjange.data.mapper

import com.junjange.data.model.remote.LotteryGetNumbersEntity
import com.junjange.domain.model.LotteryGetNumbers

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
