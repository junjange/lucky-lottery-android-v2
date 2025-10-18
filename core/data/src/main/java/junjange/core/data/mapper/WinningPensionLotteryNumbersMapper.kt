package junjange.core.data.mapper

import junjange.core.data.model.local.PensionLotteryNumberDto
import junjange.core.data.model.remote.WinningPensionLotteryNumbersEntity
import junjange.core.domain.model.WinningPensionLotteryNumbers

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
    val userNumbers =
        listOf(
            pensionLotteryNumberDto.firstNum,
            pensionLotteryNumberDto.secondNum,
            pensionLotteryNumberDto.thirdNum,
            pensionLotteryNumberDto.fourthNum,
            pensionLotteryNumberDto.fifthNum,
            pensionLotteryNumberDto.sixthNum,
        )

    val winningNumbers = listOf(firstNum, secondNum, thirdNum, fourthNum, fifthNum, sixthNum)

    return listOf(pensionLotteryNumberDto.group == group) + userNumbers.zip(winningNumbers) { user, winning -> user == winning }
}
