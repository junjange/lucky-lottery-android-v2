package junjange.core.firebase.di

import junjange.core.data.datasource.FirebaseDataSource
import junjange.core.data.datasource.GoogleDataSource
import junjange.core.firebase.datasource.FirebaseDataSourceImpl
import junjange.core.firebase.datasource.GoogleDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class DataSourceModule {
    @Binds
    @Singleton
    abstract fun bindFirebaseDataSource(firebaseDataSourceImpl: FirebaseDataSourceImpl): FirebaseDataSource

    @Binds
    @Singleton
    abstract fun bindGoogleDataSource(googleDataSourceImpl: GoogleDataSourceImpl): GoogleDataSource
}
