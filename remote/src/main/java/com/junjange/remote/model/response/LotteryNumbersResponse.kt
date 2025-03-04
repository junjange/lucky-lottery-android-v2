package com.junjange.remote.model.response

import com.junjange.data.model.remote.LotteryNumbersEntity

data class LotteryNumbersResponse(
    val round: Int,
    val winningDate: String,
    val firstNum: Int,
    val secondNum: Int,
    val thirdNum: Int,
    val fourthNum: Int,
    val fifthNum: Int,
    val sixthNum: Int,
    val bonusNum: Int,
)

internal fun LotteryNumbersResponse.toData(): LotteryNumbersEntity =
    LotteryNumbersEntity(
        round = round,
        winningDate = winningDate,
        firstNum = firstNum,
        secondNum = secondNum,
        thirdNum = thirdNum,
        fourthNum = fourthNum,
        fifthNum = fifthNum,
        sixthNum = sixthNum,
        bonusNum = bonusNum,
        prizeAmount = 0L,
        perPersonAmount = 0L,
        winnerCount = 0,
    )
