package junjange.core.remote.model.response

import junjange.core.data.model.remote.PensionLotteryNumbersEntity

data class PensionLotteryNumbersResponse(
    val pensionGroup: Int,
    val pensionFirstNum: Int,
    val pensionSecondNum: Int,
    val pensionThirdNum: Int,
    val pensionFourthNum: Int,
    val pensionFifthNum: Int,
    val pensionSixthNum: Int,
    val rank: String?,
    val checkWinningBonus: Boolean,
    val correctNumbers: List<Boolean>?,
    val bonusCorrectNumbers: List<Boolean>?,
)

internal fun List<PensionLotteryNumbersResponse>.toData() =
    map {
        PensionLotteryNumbersEntity(
            pensionGroup = it.pensionGroup,
            pensionFirstNum = it.pensionFirstNum,
            pensionSecondNum = it.pensionSecondNum,
            pensionThirdNum = it.pensionThirdNum,
            pensionFourthNum = it.pensionFourthNum,
            pensionFifthNum = it.pensionFifthNum,
            pensionSixthNum = it.pensionSixthNum,
            rank = it.rank,
            checkWinningBonus = it.checkWinningBonus,
            correctNumbers = it.correctNumbers,
            bonusCorrectNumbers = it.bonusCorrectNumbers,
        )
    }
