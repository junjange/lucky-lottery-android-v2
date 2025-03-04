package com.junjange.data.model.remote

data class PensionLotteryNumbersEntity(
    val id: Long = 0L,
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
