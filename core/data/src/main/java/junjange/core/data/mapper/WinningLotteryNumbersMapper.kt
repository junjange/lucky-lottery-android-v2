package junjange.core.data.mapper

import junjange.core.data.model.local.LotteryNumberDto
import junjange.core.data.model.remote.WinningLotteryNumbersEntity
import junjange.core.domain.model.PensionLotteryHome
import junjange.core.domain.model.WinningLotteryNumbers
import junjange.core.domain.model.WinningPensionLotteryNumbers

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
    val winningNumbers = listOf(firstNum, secondNum, thirdNum, fourthNum, fifthNum, sixthNum, bonusNum)
    val userNumbers =
        listOf(
            lotteryNumberDto.firstNum,
            lotteryNumberDto.secondNum,
            lotteryNumberDto.thirdNum,
            lotteryNumberDto.fourthNum,
            lotteryNumberDto.fifthNum,
            lotteryNumberDto.sixthNum,
        )

    var checkWinningBonus = false
    val correctNumbers =
        userNumbers.map { num ->
            (num in winningNumbers).also { if (num == bonusNum) checkWinningBonus = true }
        }

    return correctNumbers to checkWinningBonus
}
