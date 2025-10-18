package junjange.core.remote.api

import junjange.core.data.provider.AccessTokenProvider
import junjange.core.data.provider.RefreshTokenProvider
import junjange.core.remote.interceptor.AccessTokenInterceptor
import junjange.core.remote.model.request.RefreshRequest
import junjange.core.remote.model.response.JwtTokenResponse
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

internal class Authenticator constructor(
    private val apiService: ApiService,
    private val accessTokenProvider: AccessTokenProvider,
    private val refreshTokenProvider: RefreshTokenProvider,
    private val authenticationListener: AuthenticationListener,
) : Authenticator {
    override fun authenticate(
        route: Route?,
        response: Response,
    ): Request? {
        val refreshToken = refreshTokenProvider.value
        if (refreshToken.isBlank()) {
            authenticationListener.onSessionExpired()
            return null
        }
        return refresh(refreshToken).fold(
            onSuccess = {
                accessTokenProvider.value = it.accessToken
                refreshTokenProvider.value = it.refreshToken

                AccessTokenInterceptor.from(response.request, it.accessToken)
            },
            onFailure = {
                authenticationListener.onSessionExpired()
                null
            },
        )
    }

    private fun refresh(refreshToken: String): Result<JwtTokenResponse> =
        runBlocking {
            runCatching {
                val body = RefreshRequest(refreshToken = refreshToken)
                apiService.postRefresh(body = body).data
            }
        }
}
