package junjange.core.local.datasource

import junjange.core.data.datasource.PensionLotteryRoomDataSource
import junjange.core.data.model.local.PensionLotteryNumberDto
import junjange.core.local.dao.PensionLotteryDao
import junjange.core.local.model.toData
import junjange.core.local.model.toLocal

internal class PensionLotteryRoomDataSourceImpl
    
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

        override suspend fun deleteAllPensionLottery(): Result<Unit> = runCatching { dao.deleteAllPensionLottery() }
    }
