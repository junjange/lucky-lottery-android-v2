package junjange.core.firebase.datasource

import junjange.core.data.datasource.FirebaseDataSource
import junjange.core.firebase.bridge.FcmTokenBridge
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * Delegates FCM token retrieval to a Swift-provided [FcmTokenBridge].
 * If no bridge is registered in Koin, returns a descriptive failure.
 */
internal class IosFirebaseDataSource(
    private val bridge: FcmTokenBridge?,
) : FirebaseDataSource {
    override suspend fun getToken(): Result<String> =
        runCatching {
            val tokenBridge =
                bridge ?: error(
                    "FcmTokenBridge is not registered. Implement it in Swift with the " +
                        "Firebase iOS SDK and register it in Koin.",
                )
            suspendCancellableCoroutine { continuation ->
                tokenBridge.getToken(
                    onSuccess = { token -> if (continuation.isActive) continuation.resume(token) },
                    onFailure = { message ->
                        if (continuation.isActive) {
                            continuation.resumeWithException(IllegalStateException(message))
                        }
                    },
                )
            }
        }
}
