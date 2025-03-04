package com.junjange.domain.model

data class LotteryGetNumbers(
    val id: Long,
    val firstNum: Int,
    val secondNum: Int,
    val thirdNum: Int,
    val fourthNum: Int,
    val fifthNum: Int,
    val sixthNum: Int,
    val correctNumbers: List<Boolean>?,
    val rank: String?,
)
