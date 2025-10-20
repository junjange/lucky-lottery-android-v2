package junjange.core.data.repository

import junjange.core.data.datasource.FirebaseDataSource
import junjange.core.domain.repository.FirebaseRepository
import javax.inject.Inject

internal class FirebaseRepositoryImpl
    @Inject
    constructor(
        private val firebaseDataSource: FirebaseDataSource,
    ) : FirebaseRepository {
        override suspend fun getToken(): Result<String> = firebaseDataSource.getToken().mapCatching { it }
    }
