package junjange.core.data.mapper

import junjange.core.data.model.remote.LotteryRandomNumbersEntity
import junjange.core.domain.model.LotteryRandomNumbers

fun LotteryRandomNumbersEntity.toDomain() =
    LotteryRandomNumbers(
        round = round,
        winningDate = winningDate,
        firstNum = firstNum,
        secondNum = secondNum,
        thirdNum = thirdNum,
        fourthNum = fourthNum,
        fifthNum = fifthNum,
        sixthNum = sixthNum,
    )
