package com.junjange.remote.datasource

import com.junjange.data.datasource.CredentialDataSource
import com.junjange.data.model.local.JwtTokenEntity
import com.junjange.data.model.remote.AccessTokenEntity
import com.junjange.data.model.remote.IsRegisteredEntity
import com.junjange.remote.api.CredentialApiService
import com.junjange.remote.model.request.RegisterRequest
import com.junjange.remote.model.response.toData
import javax.inject.Inject

internal class CredentialDataSourceImpl @Inject constructor(
    private val apiService: CredentialApiService,
) : CredentialDataSource {

    override suspend fun postSignup2(nickName: String): Result<Unit> = runCatching {
        val body = RegisterRequest(nickName = nickName)
        apiService.postSignup2(body = body)
    }

    override suspend fun postRegister(
        idToken: String,
        provider: String,
        nickName: String,
    ): Result<JwtTokenEntity> = runCatching {
        val body = RegisterRequest(nickName = nickName)
        apiService.postRegister(
            idToken = idToken,
            provider = provider,
            body = body,
        ).data.toData()
    }

    override suspend fun postLogin(
        idToken: String,
        provider: String,
    ): Result<JwtTokenEntity> = runCatching {
        apiService.postLogin(
            idToken = idToken,
            provider = provider
        ).data.toData()
    }

    override suspend fun postLogin2(userId: String): Result<AccessTokenEntity> = runCatching {
        apiService.postLogin2(userId = userId).data.toData()
    }

    override suspend fun getValidRegister(
        idToken: String,
        provider: String,
    ): Result<IsRegisteredEntity> = runCatching {
        apiService.getValidRegister(
            idToken = idToken,
            provider = provider
        ).data.toData()
    }
}