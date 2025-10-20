package junjange.core.remote.model.response

import junjange.core.data.model.remote.LotteryRandomNumbersEntity

data class LotteryRandomNumbersResponse(
    val round: Int,
    val winningDate: String,
    val firstNum: Int,
    val secondNum: Int,
    val thirdNum: Int,
    val fourthNum: Int,
    val fifthNum: Int,
    val sixthNum: Int,
)

internal fun LotteryRandomNumbersResponse.toData(): LotteryRandomNumbersEntity =
    LotteryRandomNumbersEntity(
        round = round,
        winningDate = winningDate,
        firstNum = firstNum,
        secondNum = secondNum,
        thirdNum = thirdNum,
        fourthNum = fourthNum,
        fifthNum = fifthNum,
        sixthNum = sixthNum,
    )
