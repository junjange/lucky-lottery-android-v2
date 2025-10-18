package junjange.core.domain.model

data class LotteryNumbers(
    val round: Int,
    val winningDate: String,
    val firstNum: Int,
    val secondNum: Int,
    val thirdNum: Int,
    val fourthNum: Int,
    val fifthNum: Int,
    val sixthNum: Int,
    val bonusNum: Int,
    val prizeAmount: Long,
    val perPersonAmount: Long,
    val winnerCount: Int,
)
