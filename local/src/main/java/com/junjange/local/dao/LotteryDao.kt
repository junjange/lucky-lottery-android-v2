package com.junjange.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.junjange.local.model.LotteryEntity

@Dao
interface LotteryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLottery(lottery: LotteryEntity)

    @Query("SELECT DISTINCT round FROM lottery ORDER BY round DESC LIMIT :limit OFFSET :offset")
    suspend fun getPagedRounds(
        limit: Int,
        offset: Int,
    ): List<Int>

    @Query("SELECT * FROM lottery WHERE round IN (:rounds) ORDER BY round DESC, id ASC")
    suspend fun getLotteriesByRounds(rounds: List<Int>): List<LotteryEntity>

    @Query("DELETE FROM lottery WHERE round = :round AND id = :id")
    suspend fun deleteLotteryByRoundAndId(
        round: Int,
        id: Long,
    )
}
