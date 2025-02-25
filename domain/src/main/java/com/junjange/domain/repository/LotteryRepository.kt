package com.junjange.domain.repository

import com.junjange.domain.model.LotteryGet
import com.junjange.domain.model.LotteryGetContent
import com.junjange.domain.model.LotteryNumbers
import com.junjange.domain.model.LotteryRandomNumbers

interface LotteryRepository {
    suspend fun getLotteryRound(): Result<Int>

    suspend fun getLotteryGet(
        page: Int,
        size: Int,
    ): Result<LotteryGet>

    suspend fun postLotterySave(
        firstNum: Int,
        secondNum: Int,
        thirdNum: Int,
        fourthNum: Int,
        fifthNum: Int,
        sixthNum: Int,
    ): Result<Unit>

    suspend fun getLotteryRandom(): Result<LotteryRandomNumbers>

    suspend fun getLottoNumber(drwNo: Int): Result<LotteryNumbers>

    suspend fun loadLotteryRounds(
        page: Int,
        size: Int,
    ): Result<List<LotteryGetContent>>

    suspend fun insertLottery(
        firstNum: Int,
        secondNum: Int,
        thirdNum: Int,
        fourthNum: Int,
        fifthNum: Int,
        sixthNum: Int,
    ): Result<Unit>
}
