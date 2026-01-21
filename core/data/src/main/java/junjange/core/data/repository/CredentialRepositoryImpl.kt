package junjange.core.data.repository

import junjange.core.data.datasource.CredentialDataSource
import junjange.core.data.mapper.toDomain
import junjange.core.domain.model.IsRegistered
import junjange.core.domain.model.JwtToken
import junjange.core.domain.repository.CredentialRepository

internal class CredentialRepositoryImpl
    
    constructor(
        private val dataSource: CredentialDataSource,
    ) : CredentialRepository {
        override suspend fun postRegister(
            idToken: String,
            provider: String,
            nickName: String,
        ): Result<JwtToken> =
            dataSource.postRegister(
                idToken = idToken,
                provider = provider,
                nickName = nickName,
            ).mapCatching { it.toDomain() }

        override suspend fun postLogin(
            idToken: String,
            provider: String,
        ): Result<JwtToken> =
            dataSource.postLogin(
                idToken = idToken,
                provider = provider,
            ).mapCatching { it.toDomain() }

        override suspend fun postLogout(): Result<Unit> = dataSource.postLogout()

        override suspend fun deleteMe(oauthAccessToken: String?): Result<Unit> = dataSource.deleteMe(oauthAccessToken = oauthAccessToken)

        override suspend fun getValidRegister(
            idToken: String,
            provider: String,
        ): Result<IsRegistered> =
            dataSource.getValidRegister(
                idToken = idToken,
                provider = provider,
            ).mapCatching { it.toDomain() }
    }
