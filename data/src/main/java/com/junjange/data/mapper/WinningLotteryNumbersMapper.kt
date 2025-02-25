package com.junjange.data.mapper

import com.junjange.data.model.local.LotteryNumberDto
import com.junjange.data.model.remote.WinningLotteryNumbersEntity
import com.junjange.domain.model.PensionLotteryHome
import com.junjange.domain.model.WinningLotteryNumbers
import com.junjange.domain.model.WinningPensionLotteryNumbers

internal fun WinningLotteryNumbersEntity.toDomain() =
    WinningLotteryNumbers(
        bonusNum = bonusNum,
        firstNum = firstNum,
        secondNum = secondNum,
        thirdNum = thirdNum,
        fourthNum = fourthNum,
        fifthNum = fifthNum,
        sixthNum = sixthNum,
    )

internal fun PensionLotteryHome.toWinningPensionLotteryNumbers(): WinningPensionLotteryNumbers =
    WinningPensionLotteryNumbers(
        lotteryGroup,
        winningFirstNum,
        winningSecondNum,
        winningThirdNum,
        winningFourthNum,
        winningFifthNum,
        winningSixthNum,
    )

fun WinningLotteryNumbers.toCorrectNumbers(lotteryNumberDto: LotteryNumberDto): Pair<List<Boolean>, Boolean> {
    val correctNumbers = MutableList(6) { false }
    var checkWinningBonus = false
    if (lotteryNumberDto.firstNum == firstNum || lotteryNumberDto.firstNum == bonusNum) {
        if (lotteryNumberDto.firstNum == bonusNum) checkWinningBonus = true
        correctNumbers[0] = true
    }
    if (lotteryNumberDto.secondNum == secondNum || lotteryNumberDto.secondNum == bonusNum) {
        if (lotteryNumberDto.secondNum == bonusNum) checkWinningBonus = true
        correctNumbers[1] = true
    }
    if (lotteryNumberDto.thirdNum == thirdNum || lotteryNumberDto.thirdNum == bonusNum) {
        if (lotteryNumberDto.thirdNum == bonusNum) checkWinningBonus = true
        correctNumbers[2] = true
    }
    if (lotteryNumberDto.fourthNum == fourthNum || lotteryNumberDto.fourthNum == bonusNum) {
        if (lotteryNumberDto.fourthNum == bonusNum) checkWinningBonus = true
        correctNumbers[3] = true
    }
    if (lotteryNumberDto.fifthNum == fifthNum || lotteryNumberDto.fifthNum == bonusNum) {
        if (lotteryNumberDto.fifthNum == bonusNum) checkWinningBonus = true
        correctNumbers[4] = true
    }
    if (lotteryNumberDto.sixthNum == sixthNum || lotteryNumberDto.sixthNum == bonusNum) {
        if (lotteryNumberDto.sixthNum == bonusNum) checkWinningBonus = true
        correctNumbers[5] = true
    }

    return Pair(correctNumbers, checkWinningBonus)
}
