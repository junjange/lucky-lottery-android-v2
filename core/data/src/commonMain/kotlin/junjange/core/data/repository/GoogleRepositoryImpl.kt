package junjange.core.data.repository

import junjange.core.data.datasource.GoogleDataSource
import junjange.core.data.mapper.toDomain
import junjange.core.domain.model.GoogleOauthToken
import junjange.core.domain.repository.GoogleRepository

internal class GoogleRepositoryImpl
    
    constructor(
        private val dataSource: GoogleDataSource,
    ) : GoogleRepository {
        override suspend fun postOauthToken(
            grantType: String,
            clientId: String,
            clientSecret: String,
            redirectUri: String,
            code: String,
        ): Result<GoogleOauthToken> =
            dataSource.postOauthToken(
                grantType = grantType,
                clientId = clientId,
                clientSecret = clientSecret,
                redirectUri = redirectUri,
                code = code,
            ).mapCatching { it.toDomain() }
    }
