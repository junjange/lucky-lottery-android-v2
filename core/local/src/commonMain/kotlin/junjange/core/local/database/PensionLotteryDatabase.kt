package junjange.core.local.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import junjange.core.local.dao.PensionLotteryDao
import junjange.core.local.model.PensionLotteryEntity

@Database(entities = [PensionLotteryEntity::class], version = 1)
@ConstructedBy(PensionLotteryDatabaseConstructor::class)
abstract class PensionLotteryDatabase : RoomDatabase() {
    abstract fun pensionLotteryDao(): PensionLotteryDao
}

@Suppress("NO_ACTUAL_FOR_EXPECT", "KotlinNoActualForExpectedDeclaration")
expect object PensionLotteryDatabaseConstructor : RoomDatabaseConstructor<PensionLotteryDatabase> {
    override fun initialize(): PensionLotteryDatabase
}

internal const val PENSION_LOTTERY_DATABASE_NAME = "pension_lottery_database"
