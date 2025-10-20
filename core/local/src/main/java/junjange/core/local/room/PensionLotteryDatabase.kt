package junjange.core.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import junjange.core.local.dao.PensionLotteryDao
import junjange.core.local.model.PensionLotteryEntity

@Database(entities = [PensionLotteryEntity::class], version = 1)
abstract class PensionLotteryDatabase : RoomDatabase() {
    abstract fun pensionLotteryDao(): PensionLotteryDao
}
