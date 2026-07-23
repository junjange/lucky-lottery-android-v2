package junjange.core.local.di

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.russhwolf.settings.NSUserDefaultsSettings
import com.russhwolf.settings.Settings
import junjange.core.local.database.LOTTERY_DATABASE_NAME
import junjange.core.local.database.LotteryDatabase
import junjange.core.local.database.PENSION_LOTTERY_DATABASE_NAME
import junjange.core.local.database.PensionLotteryDatabase
import junjange.core.local.notification.IosNotificationScheduler
import junjange.core.local.notification.NotificationScheduler
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.Module
import org.koin.dsl.module
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDefaults
import platform.Foundation.NSUserDomainMask

actual val localPlatformModule: Module = module {
    single<Settings> { NSUserDefaultsSettings(NSUserDefaults.standardUserDefaults) }
    single<NotificationScheduler> { IosNotificationScheduler() }

    single {
        Room.databaseBuilder<LotteryDatabase>(name = documentPath("$LOTTERY_DATABASE_NAME.db"))
            .setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.Default)
            .build()
    }

    single {
        Room.databaseBuilder<PensionLotteryDatabase>(name = documentPath("$PENSION_LOTTERY_DATABASE_NAME.db"))
            .setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.Default)
            .build()
    }
}

@OptIn(ExperimentalForeignApi::class)
private fun documentPath(fileName: String): String {
    val documentDirectory =
        NSFileManager.defaultManager.URLForDirectory(
            directory = NSDocumentDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = false,
            error = null,
        )
    return requireNotNull(documentDirectory?.path) + "/" + fileName
}
