package junjange.core.data.model.remote

data class LotteryGetNumbersEntity(
    val id: Long = 0L,
    val firstNum: Int,
    val secondNum: Int,
    val thirdNum: Int,
    val fourthNum: Int,
    val fifthNum: Int,
    val sixthNum: Int,
    val correctNumbers: List<Boolean>?,
    val rank: String?,
)
