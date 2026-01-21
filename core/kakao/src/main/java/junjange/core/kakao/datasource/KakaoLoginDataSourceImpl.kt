package junjange.core.kakao.datasource

import android.content.Context
import junjange.core.data.datasource.KakaoLoginDataSource
import junjange.core.data.model.local.KakaoAccessTokenEntity
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

internal class KakaoLoginDataSourceImpl
    constructor(
        private val context: Context,
    ) : KakaoLoginDataSource {
        /**
         * @param context: Activity context
         */
        override suspend fun login(): Result<KakaoAccessTokenEntity> =
            runCatching {
                suspendCancellableCoroutine { continuation ->
                    val callback: (OAuthToken?, Throwable?) -> Unit = { token, throwable ->
                        when {
                            throwable != null -> continuation.resumeWithException(throwable)
                            token != null && continuation.isActive -> {
                                val accessToken =
                                    KakaoAccessTokenEntity(
                                        accessToken = token.accessToken,
                                        idToken = token.idToken!!,
                                    )
                                continuation.resume(accessToken)
                            }
                        }
                    }

                    val userApiClient = UserApiClient.instance
                    if (userApiClient.isKakaoTalkLoginAvailable(context)) {
                        userApiClient.loginWithKakaoTalk(context, callback = callback)
                    } else {
                        userApiClient.loginWithKakaoAccount(context, callback = callback)
                    }
                }
            }
    }
