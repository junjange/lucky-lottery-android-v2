package com.junjange.local.datasource

import com.junjange.data.datasource.LotteryRoomDataSource
import com.junjange.data.model.local.LotteryNumberDto
import com.junjange.local.dao.LotteryDao
import com.junjange.local.model.toData
import com.junjange.local.model.toLocal
import javax.inject.Inject

internal class LotteryRoomDataSourceImpl
    @Inject
    constructor(
        private val dao: LotteryDao,
    ) : LotteryRoomDataSource {
        override suspend fun insertLottery(lotteryNumberDto: LotteryNumberDto): Result<Unit> =
            runCatching { dao.insertLottery(lottery = lotteryNumberDto.toLocal()) }

        override suspend fun getPagedRounds(
            limit: Int,
            offset: Int,
        ): Result<List<Int>> = runCatching { dao.getPagedRounds(limit = limit, offset = offset) }

        override suspend fun getLotteriesByRound(rounds: List<Int>): Result<List<LotteryNumberDto>> =
            runCatching { dao.getLotteriesByRounds(rounds = rounds).toData() }

        override suspend fun deleteLotteryByRoundAndId(
            round: Int,
            id: Long,
        ): Result<Unit> = runCatching { dao.deleteLotteryByRoundAndId(round = round, id = id) }
    }
