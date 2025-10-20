package junjange.core.remote.datasource

import junjange.core.data.datasource.CredentialDataSource
import junjange.core.data.model.local.JwtTokenEntity
import junjange.core.data.model.remote.IsRegisteredEntity
import junjange.core.remote.api.ApiService
import junjange.core.remote.model.request.RegisterRequest
import junjange.core.remote.model.response.toData
import javax.inject.Inject

internal class CredentialDataSourceImpl
    @Inject
    constructor(
        private val apiService: ApiService,
    ) : CredentialDataSource {
        override suspend fun postRegister(
            idToken: String,
            provider: String,
            nickName: String,
        ): Result<JwtTokenEntity> =
            runCatching {
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
        ): Result<JwtTokenEntity> =
            runCatching {
                apiService.postLogin(
                    idToken = idToken,
                    provider = provider,
                ).data.toData()
            }

        override suspend fun postLogout(): Result<Unit> =
            runCatching {
                apiService.postLogout().data
            }

        override suspend fun deleteMe(oauthAccessToken: String?): Result<Unit> =
            runCatching {
                apiService.deleteMe(oauthAccessToken = oauthAccessToken).data
            }

        override suspend fun getValidRegister(
            idToken: String,
            provider: String,
        ): Result<IsRegisteredEntity> =
            runCatching {
                apiService.getValidRegister(
                    idToken = idToken,
                    provider = provider,
                ).data.toData()
            }
    }
