package com.junjange.data.repository

import com.junjange.data.datasource.LotteryDataSource
import com.junjange.data.mapper.toDomain
import com.junjange.domain.model.LotteryGet
import com.junjange.domain.model.LotteryNumbers
import com.junjange.domain.model.LotteryRandomNumbers
import com.junjange.domain.model.PensionLotteryHome
import com.junjange.domain.repository.LotteryRepository
import javax.inject.Inject

internal class LotteryRepositoryImpl
    @Inject
    constructor(
        private val dataSource: LotteryDataSource,
    ) : LotteryRepository {
        private val lottery: MutableMap<Int, LotteryNumbers> = mutableMapOf()
        private val pensionLottery: MutableMap<Int, PensionLotteryHome> = mutableMapOf()

        override suspend fun getLotteryRound(): Result<Int> = dataSource.getLotteryRound()

        override suspend fun getPensionLotteryRound(): Result<Int> = dataSource.getPensionLotteryRound()

        override suspend fun getLotteryGet(
            page: Int,
            size: Int,
        ): Result<LotteryGet> =
            dataSource
                .getLotteryGet(
                    page = page,
                    size = size,
                ).mapCatching { it.toDomain() }

        override suspend fun postLotterySave(
            firstNum: Int,
            secondNum: Int,
            thirdNum: Int,
            fourthNum: Int,
            fifthNum: Int,
            sixthNum: Int,
        ): Result<Unit> =
            dataSource.postLotterySave(
                firstNum = firstNum,
                secondNum = secondNum,
                thirdNum = thirdNum,
                fourthNum = fourthNum,
                fifthNum = fifthNum,
                sixthNum = sixthNum,
            )

        override suspend fun getLotteryRandom(): Result<LotteryRandomNumbers> = dataSource.getLotteryRandom().mapCatching { it.toDomain() }

        override suspend fun getLottoNumber(drwNo: Int): Result<LotteryNumbers> {
            lottery[drwNo]?.let {
                return Result.success(it)
            }

            val lotteryNumbers = dataSource.getLottoNumber(drwNo = drwNo).mapCatching { it.toDomain() }

            if (lotteryNumbers.isSuccess) {
                lottery[drwNo] = lotteryNumbers.getOrThrow()
            }

            return lotteryNumbers
        }

        override suspend fun getPensionLottoNumber(drwNo: Int): Result<PensionLotteryHome> {
            pensionLottery[drwNo]?.let {
                return Result.success(it)
            }

            val pensionLotteryHome =
                dataSource.getPensionLottoNumber(drwNo = drwNo).mapCatching { it.toDomain() }

            if (pensionLotteryHome.isSuccess) {
                pensionLottery[drwNo] = pensionLotteryHome.getOrThrow()
            }

            return pensionLotteryHome
        }
    }
