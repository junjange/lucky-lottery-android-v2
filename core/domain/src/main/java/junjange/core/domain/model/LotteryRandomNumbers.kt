package junjange.core.domain.model

data class LotteryRandomNumbers(
    val round: Int,
    val winningDate: String,
    val firstNum: Int,
    val secondNum: Int,
    val thirdNum: Int,
    val fourthNum: Int,
    val fifthNum: Int,
    val sixthNum: Int,
)
