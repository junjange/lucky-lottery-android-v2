package junjange.core.firebase.di

import junjange.core.data.datasource.FirebaseDataSource
import junjange.core.firebase.datasource.IosFirebaseDataSource
import org.koin.core.module.Module
import org.koin.dsl.module

actual val firebaseModule: Module = module {
    single<FirebaseDataSource> { IosFirebaseDataSource(getOrNull()) }
}
