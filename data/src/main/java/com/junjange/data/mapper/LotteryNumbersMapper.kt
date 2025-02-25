package com.junjange.data.mapper

import com.junjange.data.model.local.LotteryNumberDto
import com.junjange.data.model.remote.LotteryNumbersEntity
import com.junjange.domain.model.LotteryNumbers
import com.junjange.domain.model.WinningLotteryNumbers

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
