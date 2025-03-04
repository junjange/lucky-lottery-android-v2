package com.junjange.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.junjange.local.model.PensionLotteryEntity

@Dao
interface PensionLotteryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPensionLottery(pensionLottery: PensionLotteryEntity)

    @Query("SELECT DISTINCT round FROM pension_lottery ORDER BY round DESC LIMIT :limit OFFSET :offset")
    suspend fun getPagedRounds(
        limit: Int,
        offset: Int,
    ): List<Int>

    @Query("SELECT * FROM pension_lottery WHERE round IN (:rounds) ORDER BY round DESC, id DESC")
    suspend fun getPensionLotteriesByRounds(rounds: List<Int>): List<PensionLotteryEntity>

    @Query("DELETE FROM pension_lottery WHERE round = :round AND id = :id")
    suspend fun deletePensionLotteryByRoundAndId(
        round: Int,
        id: Long,
    )
}
