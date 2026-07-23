package junjange.core.firebase.di

import junjange.core.data.datasource.GoogleDataSource
import junjange.core.firebase.datasource.GoogleDataSourceImpl
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * Google OAuth token-exchange data source (platform-agnostic HTTP).
 * The FCM [junjange.core.data.datasource.FirebaseDataSource] lives in [firebaseModule].
 */
val firebaseDataSourceModule = module {
    single<GoogleDataSource> { GoogleDataSourceImpl(get()) }
}

/** Provides the platform FCM [junjange.core.data.datasource.FirebaseDataSource]. */
expect val firebaseModule: Module
