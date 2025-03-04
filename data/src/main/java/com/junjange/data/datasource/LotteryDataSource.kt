package com.junjange.data.datasource

import com.junjange.data.model.remote.LotteryGetEntity
import com.junjange.data.model.remote.LotteryRandomNumbersEntity
import com.junjange.data.model.remote.LottoEntity
import com.junjange.data.model.remote.PensionLotteryHomeEntity

interface LotteryDataSource {
    suspend fun getLotteryRound(): Result<Int>

    suspend fun getPensionLotteryRound(): Result<Int>

    suspend fun getLotteryGet(
        page: Int,
        size: Int,
    ): Result<LotteryGetEntity>

    suspend fun postLotterySave(
        firstNum: Int,
        secondNum: Int,
        thirdNum: Int,
        fourthNum: Int,
        fifthNum: Int,
        sixthNum: Int,
    ): Result<Unit>

    suspend fun getLotteryRandom(): Result<LotteryRandomNumbersEntity>

    suspend fun getLottoNumber(drwNo: Int): Result<LottoEntity>

    suspend fun getPensionLottoNumber(drwNo: Int): Result<PensionLotteryHomeEntity>
}
