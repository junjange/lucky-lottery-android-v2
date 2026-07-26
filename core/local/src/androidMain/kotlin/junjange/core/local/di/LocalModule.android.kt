package junjange.core.local.di

import android.preference.PreferenceManager
import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import androidx.work.WorkManager
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings
import junjange.core.local.database.LOTTERY_DATABASE_NAME
import junjange.core.local.database.LotteryDatabase
import junjange.core.local.database.PENSION_LOTTERY_DATABASE_NAME
import junjange.core.local.database.PensionLotteryDatabase
import junjange.core.local.notification.AndroidNotificationScheduler
import junjange.core.local.notification.NotificationScheduler
import junjange.core.notification.LottoNotificationManager
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

actual val localPlatformModule: Module = module {
    single<Settings> {
        SharedPreferencesSettings(PreferenceManager.getDefaultSharedPreferences(androidContext()))
    }

    single { WorkManager.getInstance(androidContext()) }
    single<NotificationScheduler> { AndroidNotificationScheduler(workManager = get()) }
    single { LottoNotificationManager(androidContext(), get()) }

    single {
        val context = androidContext().applicationContext
        Room.databaseBuilder<LotteryDatabase>(
            context = context,
            name = context.getDatabasePath(LOTTERY_DATABASE_NAME).absolutePath,
        ).setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO)
            .build()
    }

    single {
        val context = androidContext().applicationContext
        Room.databaseBuilder<PensionLotteryDatabase>(
            context = context,
            name = context.getDatabasePath(PENSION_LOTTERY_DATABASE_NAME).absolutePath,
        ).setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO)
            .build()
    }
}
