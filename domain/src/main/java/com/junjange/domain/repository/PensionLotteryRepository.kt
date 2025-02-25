package com.junjange.domain.repository

import com.junjange.domain.model.LotteryGet
import com.junjange.domain.model.LotteryGetContent
import com.junjange.domain.model.PensionLotteryGet
import com.junjange.domain.model.PensionLotteryRandom

interface PensionLotteryRepository {
    suspend fun postPensionLotterySave(
        pensionGroup: Int,
        pensionFirstNum: Int,
        pensionSecondNum: Int,
        pensionThirdNum: Int,
        pensionFourthNum: Int,
        pensionFifthNum: Int,
        pensionSixthNum: Int,
    ): Result<Unit>

    suspend fun getPensionLotteryRandom(): Result<PensionLotteryRandom>

    suspend fun getPensionLotteryGet(
        page: Int,
        size: Int,
    ): Result<PensionLotteryGet>

    suspend fun getPensionLotteryRound(): Result<Int>

    suspend fun getLotteryGet(
        page: Int,
        size: Int,
    ): Result<LotteryGet>

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
