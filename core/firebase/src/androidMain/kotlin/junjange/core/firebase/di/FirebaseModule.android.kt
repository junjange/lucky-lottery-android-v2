package junjange.core.firebase.di

import com.google.firebase.messaging.FirebaseMessaging
import junjange.core.data.datasource.FirebaseDataSource
import junjange.core.firebase.datasource.FirebaseDataSourceImpl
import org.koin.core.module.Module
import org.koin.dsl.module

actual val firebaseModule: Module = module {
    single { FirebaseMessaging.getInstance() }
    single<FirebaseDataSource> { FirebaseDataSourceImpl(get()) }
}
