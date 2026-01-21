package junjange.core.firebase.di

import junjange.core.data.datasource.FirebaseDataSource
import junjange.core.data.datasource.GoogleDataSource
import junjange.core.firebase.datasource.FirebaseDataSourceImpl
import junjange.core.firebase.datasource.GoogleDataSourceImpl
import org.koin.dsl.module

val firebaseDataSourceModule = module {
    single<FirebaseDataSource> { FirebaseDataSourceImpl(get()) }
    single<GoogleDataSource> { GoogleDataSourceImpl(get()) }
}
