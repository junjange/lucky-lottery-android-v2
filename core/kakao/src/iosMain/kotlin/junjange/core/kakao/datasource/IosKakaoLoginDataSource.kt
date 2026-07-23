package junjange.core.kakao.datasource

import junjange.core.data.datasource.KakaoLoginDataSource
import junjange.core.data.model.local.KakaoAccessTokenEntity
import junjange.core.kakao.bridge.KakaoLoginBridge
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * Delegates to a Swift-provided [KakaoLoginBridge] (backed by the Kakao iOS SDK).
 * If no bridge is registered in Koin, login fails with a descriptive error.
 */
internal class IosKakaoLoginDataSource(
    private val bridge: KakaoLoginBridge?,
) : KakaoLoginDataSource {
    override suspend fun login(): Result<KakaoAccessTokenEntity> =
        runCatching {
            val loginBridge =
                bridge ?: error(
                    "KakaoLoginBridge is not registered. Implement it in Swift with the " +
                        "Kakao iOS SDK and register it in Koin before calling login().",
                )
            suspendCancellableCoroutine { continuation ->
                loginBridge.login(
                    onSuccess = { accessToken, idToken ->
                        if (continuation.isActive) {
                            continuation.resume(
                                KakaoAccessTokenEntity(accessToken = accessToken, idToken = idToken),
                            )
                        }
                    },
                    onFailure = { message ->
                        if (continuation.isActive) {
                            continuation.resumeWithException(IllegalStateException(message))
                        }
                    },
                )
            }
        }
}
