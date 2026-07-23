package junjange.core.firebase.engine

import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.okhttp.OkHttp

actual fun googleHttpEngine(): HttpClientEngineFactory<*> = OkHttp
