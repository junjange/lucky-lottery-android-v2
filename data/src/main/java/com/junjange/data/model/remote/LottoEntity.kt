package com.junjange.data.model.remote

import com.junjange.domain.model.LotteryNumbers

data class LottoEntity(
    val totSellamnt: Long,
    val returnValue: String,
    val drwNoDate: String,
    val firstWinamnt: Long,
    val drwtNo6: Int,
    val drwtNo4: Int,
    val firstPrzwnerCo: Int,
    val drwtNo5: Int,
    val bnusNo: Int,
    val firstAccumamnt: Long,
    val drwNo: Int,
    val drwtNo2: Int,
    val drwtNo3: Int,
    val drwtNo1: Int,
) {
    fun toDomain(): LotteryNumbers =
        LotteryNumbers(
            round = drwNo,
            winningDate = drwNoDate,
            firstNum = drwtNo1,
            secondNum = drwtNo2,
            thirdNum = drwtNo3,
            fourthNum = drwtNo4,
            fifthNum = drwtNo5,
            sixthNum = drwtNo6,
            bonusNum = bnusNo,
            prizeAmount = firstAccumamnt,
            perPersonAmount = firstWinamnt,
            winnerCount = firstPrzwnerCo,
        )
}
