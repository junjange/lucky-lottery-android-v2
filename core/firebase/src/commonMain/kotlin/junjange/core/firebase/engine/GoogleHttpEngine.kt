package junjange.core.firebase.engine

import io.ktor.client.engine.HttpClientEngineFactory

/** Platform Ktor engine used by the Google OAuth HTTP client. */
expect fun googleHttpEngine(): HttpClientEngineFactory<*>
