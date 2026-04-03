package junjange.core.remote.di

import de.jensklingenberg.ktorfit.Ktorfit
import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.Auth
import junjange.core.remote.engine.platformEngine
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import junjange.core.data.provider.AccessTokenProvider
import junjange.core.data.provider.RefreshTokenProvider
import junjange.core.remote.api.ApiService
import junjange.core.remote.api.AuthenticationListener
import junjange.core.remote.api.BaseUrl
import junjange.core.remote.api.LotteryService
import junjange.core.remote.model.request.RefreshRequest
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val remoteModule = module {
    // JSON configuration
    single {
        Json {
            ignoreUnknownKeys = true
            isLenient = true
            encodeDefaults = true
            prettyPrint = true
        }
    }

    // Main HttpClient for ApiService
    single<HttpClient>(qualifier = org.koin.core.qualifier.named("mainHttpClient")) {
        val baseUrl: BaseUrl = get()
        val accessTokenProvider: AccessTokenProvider = get()
        val refreshTokenProvider: RefreshTokenProvider = get()
        val authenticationListener: AuthenticationListener = get()
        val json: Json = get()

        HttpClient(platformEngine()) {
            // Content negotiation
            install(ContentNegotiation) {
                json(json)
            }

            // Logging
            install(Logging) {
                level = LogLevel.INFO
            }

            // Authentication with bearer token
            install(Auth) {
                bearer {
                    loadTokens {
                        val accessToken = accessTokenProvider.value
                        if (accessToken.isNotBlank()) {
                            BearerTokens(accessToken, refreshTokenProvider.value)
                        } else {
                            null
                        }
                    }

                    refreshTokens {
                        val refreshToken = refreshTokenProvider.value
                        if (refreshToken.isBlank()) {
                            authenticationListener.onSessionExpired()
                            null
                        } else {
                            try {
                                val refreshApiService: ApiService = get(qualifier = org.koin.core.qualifier.named("refreshApiService"))
                                val response = refreshApiService.postRefresh(RefreshRequest(refreshToken))
                                val tokens = response.data

                                accessTokenProvider.value = tokens.accessToken
                                refreshTokenProvider.value = tokens.refreshToken

                                BearerTokens(tokens.accessToken, tokens.refreshToken)
                            } catch (e: Exception) {
                                authenticationListener.onSessionExpired()
                                null
                            }
                        }
                    }
                }
            }

            // Default request configuration
            defaultRequest {
                url(baseUrl.url)
            }
        }
    }

    // Refresh HttpClient (without Auth plugin to avoid circular dependency)
    single<HttpClient>(qualifier = org.koin.core.qualifier.named("refreshHttpClient")) {
        val baseUrl: BaseUrl = get()
        val json: Json = get()

        HttpClient(platformEngine()) {
            install(ContentNegotiation) {
                json(json)
            }

            install(Logging) {
                level = LogLevel.INFO
            }

            defaultRequest {
                url(baseUrl.url)
            }
        }
    }

    // Lottery HttpClient
    single<HttpClient>(qualifier = org.koin.core.qualifier.named("lotteryHttpClient")) {
        val json: Json = get()

        HttpClient(platformEngine()) {
            install(ContentNegotiation) {
                json(json)
            }

            install(Logging) {
                level = LogLevel.INFO
            }

            defaultRequest {
                url("https://dhlottery.co.kr")
            }
        }
    }

    // Refresh ApiService (for token refresh)
    single<ApiService>(qualifier = org.koin.core.qualifier.named("refreshApiService")) {
        val refreshHttpClient: HttpClient = get(qualifier = org.koin.core.qualifier.named("refreshHttpClient"))
        Ktorfit.Builder()
            .httpClient(refreshHttpClient)
            .build()
            .create()
    }

    // Main ApiService
    single<ApiService> {
        val mainHttpClient: HttpClient = get(qualifier = org.koin.core.qualifier.named("mainHttpClient"))
        Ktorfit.Builder()
            .httpClient(mainHttpClient)
            .build()
            .create()
    }

    // LotteryService
    single<LotteryService> {
        val lotteryHttpClient: HttpClient = get(qualifier = org.koin.core.qualifier.named("lotteryHttpClient"))
        Ktorfit.Builder()
            .httpClient(lotteryHttpClient)
            .build()
            .create()
    }
}
