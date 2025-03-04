package com.junjange.data.datasource

import com.junjange.data.model.local.LotteryNumberDto

interface LotteryRoomDataSource {
    suspend fun insertLottery(lotteryNumberDto: LotteryNumberDto): Result<Unit>

    suspend fun getPagedRounds(
        limit: Int,
        offset: Int,
    ): Result<List<Int>>

    suspend fun getLotteriesByRound(rounds: List<Int>): Result<List<LotteryNumberDto>>

    suspend fun deleteLotteryByRoundAndId(
        round: Int,
        id: Long,
    ): Result<Unit>
}
