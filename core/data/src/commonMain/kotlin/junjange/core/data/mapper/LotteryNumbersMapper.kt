package junjange.core.data.mapper

import junjange.core.data.model.local.LotteryNumberDto
import junjange.core.data.model.remote.LotteryNumbersEntity
import junjange.core.domain.model.LotteryNumbers
import junjange.core.domain.model.WinningLotteryNumbers

fun LotteryNumbersEntity.toDomain() =
    LotteryNumbers(
        round = round,
        winningDate = winningDate,
        firstNum = firstNum,
        secondNum = secondNum,
        thirdNum = thirdNum,
        fourthNum = fourthNum,
        fifthNum = fifthNum,
        sixthNum = sixthNum,
        bonusNum = bonusNum,
        prizeAmount = prizeAmount,
        perPersonAmount = perPersonAmount,
        winnerCount = winnerCount,
    )

fun LotteryNumbers.toWinningLotteryNumbers(): WinningLotteryNumbers =
    WinningLotteryNumbers(
        bonusNum,
        firstNum,
        secondNum,
        thirdNum,
        fourthNum,
        fifthNum,
        sixthNum,
    )

fun LotteryNumbers.toData() =
    LotteryNumberDto(
        round = round,
        firstNum = firstNum,
        secondNum = secondNum,
        thirdNum = thirdNum,
        fourthNum = fourthNum,
        fifthNum = fifthNum,
        sixthNum = sixthNum,
    )
