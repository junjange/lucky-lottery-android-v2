package junjange.core.domain.repository

import junjange.core.domain.model.GoogleOauthToken

interface GoogleRepository {
    suspend fun postOauthToken(
        grantType: String,
        clientId: String,
        clientSecret: String,
        redirectUri: String,
        code: String,
    ): Result<GoogleOauthToken>
}
