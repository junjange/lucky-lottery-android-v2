package junjange.core.domain.repository

import junjange.core.domain.model.IsRegistered
import junjange.core.domain.model.JwtToken

interface CredentialRepository {
    suspend fun postRegister(
        idToken: String,
        provider: String,
        nickName: String,
    ): Result<JwtToken>

    suspend fun postLogin(
        idToken: String,
        provider: String,
    ): Result<JwtToken>

    suspend fun postLogout(): Result<Unit>

    suspend fun deleteMe(oauthAccessToken: String?): Result<Unit>

    suspend fun getValidRegister(
        idToken: String,
        provider: String,
    ): Result<IsRegistered>
}
