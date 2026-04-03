package junjange.core.domain.model

data class PensionLotteryNumbers(
    val id: Long,
    val group: Int,
    val firstNum: Int,
    val secondNum: Int,
    val thirdNum: Int,
    val fourthNum: Int,
    val fifthNum: Int,
    val sixthNum: Int,
    val rank: String?,
    val checkWinningBonus: Boolean,
    val correctNumbers: List<Boolean>?,
    val bonusCorrectNumbers: List<Boolean>?,
)
