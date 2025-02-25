package com.junjange.data.mapper

import com.junjange.data.model.local.PensionLotteryNumberDto
import com.junjange.data.model.remote.WinningPensionLotteryNumbersEntity
import com.junjange.domain.model.WinningPensionLotteryNumbers

internal fun WinningPensionLotteryNumbersEntity.toDomain() =
    WinningPensionLotteryNumbers(
        group = lotteryGroup,
        firstNum = winningFirstNum,
        secondNum = winningSecondNum,
        thirdNum = winningThirdNum,
        fourthNum = winningFourthNum,
        fifthNum = winningFifthNum,
        sixthNum = winningSixthNum,
    )

fun WinningPensionLotteryNumbers.toCorrectNumbers(pensionLotteryNumberDto: PensionLotteryNumberDto): List<Boolean> {
    val correctNumbers = MutableList(6) { false }
    if (pensionLotteryNumberDto.firstNum == firstNum) {
        correctNumbers[0] = true
    }
    if (pensionLotteryNumberDto.secondNum == secondNum) {
        correctNumbers[1] = true
    }
    if (pensionLotteryNumberDto.thirdNum == thirdNum) {
        correctNumbers[2] = true
    }
    if (pensionLotteryNumberDto.fourthNum == fourthNum) {
        correctNumbers[3] = true
    }
    if (pensionLotteryNumberDto.fifthNum == fifthNum) {
        correctNumbers[4] = true
    }
    if (pensionLotteryNumberDto.sixthNum == sixthNum) {
        correctNumbers[5] = true
    }

    return correctNumbers
}
