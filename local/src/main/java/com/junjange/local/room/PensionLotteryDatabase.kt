package com.junjange.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.junjange.local.dao.PensionLotteryDao
import com.junjange.local.model.PensionLotteryEntity

@Database(entities = [PensionLotteryEntity::class], version = 1)
abstract class PensionLotteryDatabase : RoomDatabase() {
    abstract fun pensionLotteryDao(): PensionLotteryDao
}
