package junjange.core.firebase.bridge

/**
 * Platform bridge for retrieving the Firebase Cloud Messaging registration token.
 *
 * Android reads it directly from the Firebase SDK. iOS has no Kotlin/Native binding
 * for Firebase, so the iOS app implements this in Swift (using the Firebase iOS SDK)
 * and registers it in Koin; [junjange.core.data.datasource.FirebaseDataSource] then
 * delegates to it.
 */
interface FcmTokenBridge {
    fun getToken(
        onSuccess: (token: String) -> Unit,
        onFailure: (message: String) -> Unit,
    )
}
