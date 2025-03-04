package com.junjange.domain.repository

import com.junjange.domain.model.PensionLotteryGet
import com.junjange.domain.model.PensionLotteryGetContent
import com.junjange.domain.model.PensionLotteryHome
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

    suspend fun getPensionLottoNumber(drwNo: Int): Result<PensionLotteryHome>

    suspend fun loadPensionLotteryRounds(
        page: Int,
        size: Int,
    ): Result<List<PensionLotteryGetContent>>

    suspend fun insertPensionLottery(
        group: Int,
        firstNum: Int,
        secondNum: Int,
        thirdNum: Int,
        fourthNum: Int,
        fifthNum: Int,
        sixthNum: Int,
    ): Result<Unit>

    suspend fun deletePensionLotteryByRoundAndId(
        round: Int,
        id: Long,
    ): Result<Unit>
}
