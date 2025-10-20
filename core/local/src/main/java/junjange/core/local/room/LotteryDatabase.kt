package junjange.core.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import junjange.core.local.dao.LotteryDao
import junjange.core.local.model.LotteryEntity

@Database(entities = [LotteryEntity::class], version = 1)
abstract class LotteryDatabase : RoomDatabase() {
    abstract fun lotteryDao(): LotteryDao
}
