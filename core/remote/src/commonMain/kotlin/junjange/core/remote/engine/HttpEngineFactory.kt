package junjange.core.remote.engine

import io.ktor.client.engine.HttpClientEngineFactory

expect fun platformEngine(): HttpClientEngineFactory<*>
