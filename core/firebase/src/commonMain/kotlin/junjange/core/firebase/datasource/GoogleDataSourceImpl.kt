package junjange.core.firebase.datasource

import junjange.core.data.datasource.GoogleDataSource
import junjange.core.data.model.remote.GoogleOauthTokenEntity
import junjange.core.firebase.api.GoogleApiService
import junjange.core.firebase.model.request.GoogleOauthTokenRequest
import junjange.core.firebase.model.response.toData

internal class GoogleDataSourceImpl
    
    constructor(
        private val apiService: GoogleApiService,
    ) : GoogleDataSource {
        override suspend fun postOauthToken(
            grantType: String,
            clientId: String,
            clientSecret: String,
            redirectUri: String,
            code: String,
        ): Result<GoogleOauthTokenEntity> =
            runCatching {
                val body =
                    GoogleOauthTokenRequest(
                        grantType = grantType,
                        clientId = clientId,
                        clientSecret = clientSecret,
                        redirectUri = redirectUri,
                        code = code,
                    )
                apiService.postOauthToken(
                    body = body,
                ).toData()
            }
    }
