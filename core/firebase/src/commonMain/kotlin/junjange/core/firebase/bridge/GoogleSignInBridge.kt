package junjange.core.firebase.bridge

/**
 * Platform bridge for Google Sign-In.
 *
 * Android performs sign-in via an ActivityResultContract directly in the screen, so it
 * does not use this. iOS has no Kotlin/Native binding for Google Sign-In, so the iOS app
 * implements this in Swift (using the GoogleSignIn iOS SDK) and registers it in Koin.
 */
interface GoogleSignInBridge {
    fun signIn(onResult: (idToken: String?) -> Unit)
}
