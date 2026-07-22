package junjange.core.data.datasource

import junjange.core.data.model.local.PensionLotteryNumberDto

interface PensionLotteryRoomDataSource {
    suspend fun insertPensionLottery(pensionLotteryNumberDto: PensionLotteryNumberDto): Result<Unit>

    suspend fun getPagedRounds(
        limit: Int,
        offset: Int,
    ): Result<List<Int>>

    suspend fun getPensionLotteriesByRound(rounds: List<Int>): Result<List<PensionLotteryNumberDto>>

    suspend fun deletePensionLotteryByRoundAndId(
        round: Int,
        id: Long,
    ): Result<Unit>

    suspend fun deleteAllPensionLottery(): Result<Unit>
}
