package junjange.core.data.repository

import junjange.core.data.datasource.LocalDataSource
import junjange.core.data.mapper.toData
import junjange.core.data.mapper.toDomain
import junjange.core.domain.model.JwtToken
import junjange.core.domain.repository.LocalRepository
import javax.inject.Inject

internal class LocalRepositoryImpl
    @Inject
    constructor(
        private val localDataSource: LocalDataSource,
    ) : LocalRepository {
        override suspend fun getJwtToken(): Result<JwtToken?> = localDataSource.getJwtToken().map { it?.toDomain() }

        override suspend fun getIdToken(): Result<String?> = localDataSource.getIdToken()

        override suspend fun deleteLocalData(): Result<Unit> = localDataSource.deleteLocalData()

        override suspend fun saveJwtToken(jwtToken: JwtToken): Result<Unit> =
            localDataSource.saveJwtToken(jwtTokenEntity = jwtToken.toData())

        override suspend fun saveIdToken(idToken: String): Result<Unit> = localDataSource.saveIdToken(idToken = idToken)
    }
