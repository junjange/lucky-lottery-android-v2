package junjange.core.remote.di

import junjange.core.data.provider.AccessTokenProvider
import junjange.core.data.provider.RefreshTokenProvider
import junjange.core.remote.api.ApiService
import junjange.core.remote.api.AuthenticationListener
import junjange.core.remote.api.Authenticator
import junjange.core.remote.api.BaseUrl
import junjange.core.remote.api.LotteryService
import junjange.core.remote.interceptor.AccessTokenInterceptor
import junjange.core.remote.interceptor.ErrorResponseInterceptor
import junjange.core.remote.interceptor.Interceptors
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private fun createOkHttpClient(
    interceptors: Interceptors,
    apply: OkHttpClient.Builder.() -> Unit = { },
) = OkHttpClient
    .Builder()
    .apply {
        interceptors.interceptors.forEach(::addInterceptor)
    }.apply(apply)
    .build()

val remoteModule = module {
    single<ApiService> {
        val baseUrl: BaseUrl = get()
        val interceptors: Interceptors = get()
        val accessTokenProvider: AccessTokenProvider = get()
        val refreshTokenProvider: RefreshTokenProvider = get()
        val authenticationListener: AuthenticationListener = get()

        val refreshApiService = Retrofit
            .Builder()
            .baseUrl(baseUrl.url)
            .client(createOkHttpClient(interceptors))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)

        val authenticator = Authenticator(
            apiService = refreshApiService,
            accessTokenProvider = accessTokenProvider,
            refreshTokenProvider = refreshTokenProvider,
            authenticationListener = authenticationListener,
        )

        Retrofit
            .Builder()
            .baseUrl(baseUrl.url)
            .client(
                createOkHttpClient(interceptors) {
                    addInterceptor(AccessTokenInterceptor(accessTokenProvider))
                    authenticator(authenticator)
                    addInterceptor(ErrorResponseInterceptor())
                },
            ).addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    single<LotteryService> {
        val interceptors: Interceptors = get()
        Retrofit
            .Builder()
            .baseUrl("https://dhlottery.co.kr")
            .client(
                createOkHttpClient(interceptors) {
                    addInterceptor(ErrorResponseInterceptor())
                },
            ).addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(LotteryService::class.java)
    }
}
