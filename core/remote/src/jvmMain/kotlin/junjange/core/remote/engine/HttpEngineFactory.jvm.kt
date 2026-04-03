package junjange.core.remote.engine

import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.okhttp.OkHttp

actual fun platformEngine(): HttpClientEngineFactory<*> = OkHttp
