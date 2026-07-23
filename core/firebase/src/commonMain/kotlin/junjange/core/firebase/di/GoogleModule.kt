package junjange.core.firebase.di

import de.jensklingenberg.ktorfit.Ktorfit
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import junjange.core.firebase.api.GoogleApiService
import junjange.core.firebase.engine.googleHttpEngine
import kotlinx.serialization.json.Json
import org.koin.core.qualifier.named
import org.koin.dsl.module

val googleModule = module {
    single(named("googleJson")) {
        Json {
            ignoreUnknownKeys = true
            isLenient = true
            encodeDefaults = true
            prettyPrint = true
        }
    }

    single<HttpClient>(named("googleHttpClient")) {
        val json: Json = get(named("googleJson"))

        HttpClient(googleHttpEngine()) {
            install(ContentNegotiation) {
                json(json)
            }
            install(Logging) {
                level = LogLevel.BODY
            }
        }
    }

    single<GoogleApiService> {
        val httpClient: HttpClient = get(named("googleHttpClient"))

        Ktorfit.Builder()
            .baseUrl("https://www.googleapis.com")
            .httpClient(httpClient)
            .build()
            .create()
    }
}
