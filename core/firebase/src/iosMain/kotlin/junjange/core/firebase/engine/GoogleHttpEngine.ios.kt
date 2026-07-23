package junjange.core.firebase.engine

import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.darwin.Darwin

actual fun googleHttpEngine(): HttpClientEngineFactory<*> = Darwin
