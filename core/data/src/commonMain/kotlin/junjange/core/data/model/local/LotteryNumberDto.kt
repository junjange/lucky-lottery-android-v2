package junjange.core.data.model.local

data class LotteryNumberDto(
    val round: Int,
    val id: Long = 0L,
    val firstNum: Int,
    val secondNum: Int,
    val thirdNum: Int,
    val fourthNum: Int,
    val fifthNum: Int,
    val sixthNum: Int,
)
