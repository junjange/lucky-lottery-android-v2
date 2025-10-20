package junjange.core.remote.interceptor

import junjange.core.data.provider.AccessTokenProvider
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class AccessTokenInterceptor(
    private val accessTokenProvider: AccessTokenProvider,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val accessToken = accessTokenProvider.value

        return if (accessToken.isBlank()) {
            chain.proceed(chain.request())
        } else {
            val request = from(chain.request(), accessToken)
            chain.proceed(request)
        }
    }

    companion object {
        private const val AUTHORIZATION = "Authorization"

        fun from(
            request: Request,
            accessToken: String,
        ): Request =
            request.newBuilder()
                .removeHeader(AUTHORIZATION)
                .addHeader(AUTHORIZATION, "Bearer $accessToken")
                .build()
    }
}
