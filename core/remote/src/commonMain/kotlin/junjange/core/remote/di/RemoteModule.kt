package junjange.core.remote.di

import de.jensklingenberg.ktorfit.Ktorfit
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import junjange.core.remote.api.LotteryService
import junjange.core.remote.engine.platformEngine
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

    // LotteryService
    single<LotteryService> {
        val lotteryHttpClient: HttpClient = get(qualifier = org.koin.core.qualifier.named("lotteryHttpClient"))
        Ktorfit.Builder()
            .httpClient(lotteryHttpClient)
            .build()
            .create()
    }
}
