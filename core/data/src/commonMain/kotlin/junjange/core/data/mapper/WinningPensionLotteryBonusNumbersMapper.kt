package junjange.core.data.mapper

import junjange.core.data.model.local.PensionLotteryNumberDto
import junjange.core.data.model.remote.WinningPensionLotteryBonusNumbersEntity
import junjange.core.domain.model.PensionLotteryHome
import junjange.core.domain.model.WinningPensionLotteryBonusNumbers

internal fun WinningPensionLotteryBonusNumbersEntity.toDomain() =
    WinningPensionLotteryBonusNumbers(
        firstNum = bonusFirstNum,
        secondNum = bonusSecondNum,
        thirdNum = bonusThirdNum,
        fourthNum = bonusFourthNum,
        fifthNum = bonusFifthNum,
        sixthNum = bonusSixthNum,
    )

internal fun PensionLotteryHome.toWinningPensionLotteryBonusNumbers(): WinningPensionLotteryBonusNumbers =
    WinningPensionLotteryBonusNumbers(
        firstNum = bonusFirstNum,
        secondNum = bonusSecondNum,
        thirdNum = bonusThirdNum,
        fourthNum = bonusFourthNum,
        fifthNum = bonusFifthNum,
        sixthNum = bonusSixthNum,
    )

internal fun WinningPensionLotteryBonusNumbers.toBonusCorrectNumbers(pensionLotteryNumberDto: PensionLotteryNumberDto): List<Boolean> {
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
