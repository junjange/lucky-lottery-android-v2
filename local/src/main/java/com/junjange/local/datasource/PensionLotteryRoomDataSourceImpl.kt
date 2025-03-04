package com.junjange.local.datasource

import com.junjange.data.datasource.PensionLotteryRoomDataSource
import com.junjange.data.model.local.PensionLotteryNumberDto
import com.junjange.local.dao.PensionLotteryDao
import com.junjange.local.model.toData
import com.junjange.local.model.toLocal
import javax.inject.Inject

internal class PensionLotteryRoomDataSourceImpl
    @Inject
    constructor(
        private val dao: PensionLotteryDao,
    ) : PensionLotteryRoomDataSource {
        override suspend fun insertPensionLottery(pensionLotteryNumberDto: PensionLotteryNumberDto): Result<Unit> =
            runCatching { dao.insertPensionLottery(pensionLottery = pensionLotteryNumberDto.toLocal()) }

        override suspend fun getPagedRounds(
            limit: Int,
            offset: Int,
        ): Result<List<Int>> = runCatching { dao.getPagedRounds(limit = limit, offset = offset) }

        override suspend fun getPensionLotteriesByRound(rounds: List<Int>): Result<List<PensionLotteryNumberDto>> =
            runCatching { dao.getPensionLotteriesByRounds(rounds = rounds).toData() }

        override suspend fun deletePensionLotteryByRoundAndId(
            round: Int,
            id: Long,
        ): Result<Unit> = runCatching { dao.deletePensionLotteryByRoundAndId(round = round, id = id) }
    }
