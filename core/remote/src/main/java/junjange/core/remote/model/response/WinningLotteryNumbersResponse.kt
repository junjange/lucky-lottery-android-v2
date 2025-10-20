package junjange.core.remote.model.response

import junjange.core.data.model.remote.WinningLotteryNumbersEntity

data class WinningLotteryNumbersResponse(
    val bonusNum: Int,
    val firstNum: Int,
    val secondNum: Int,
    val thirdNum: Int,
    val fourthNum: Int,
    val fifthNum: Int,
    val sixthNum: Int,
)

internal fun WinningLotteryNumbersResponse.toData() =
    WinningLotteryNumbersEntity(
        bonusNum = bonusNum,
        firstNum = firstNum,
        secondNum = secondNum,
        thirdNum = thirdNum,
        fourthNum = fourthNum,
        fifthNum = fifthNum,
        sixthNum = sixthNum,
    )
