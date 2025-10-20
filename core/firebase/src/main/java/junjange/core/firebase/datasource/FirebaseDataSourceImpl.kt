package junjange.core.firebase.datasource

import com.google.firebase.messaging.FirebaseMessaging
import junjange.core.data.datasource.FirebaseDataSource
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

internal class FirebaseDataSourceImpl
    @Inject
    constructor(
        private val messaging: FirebaseMessaging,
    ) : FirebaseDataSource {
        override suspend fun getToken(): Result<String> =
            suspendCancellableCoroutine { cancellable ->
                messaging.token.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        task.result?.let { token ->
                            cancellable.resume(Result.success(token))
                        }
                    } else {
                        cancellable.resume(Result.failure(Exception("No FCM token")))
                    }
                }
            }
    }
