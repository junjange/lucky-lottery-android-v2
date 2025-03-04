package com.junjange.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.junjange.local.dao.LotteryDao
import com.junjange.local.model.LotteryEntity

@Database(entities = [LotteryEntity::class], version = 1)
abstract class LotteryDatabase : RoomDatabase() {
    abstract fun lotteryDao(): LotteryDao
}
