package junjange.feature.login

import androidx.compose.runtime.Composable

/** A stable per-install device identifier (Android ANDROID_ID / iOS identifierForVendor). */
@Composable
expect fun rememberDeviceId(): String

/**
 * Returns a trigger that starts the platform Google sign-in flow, delivering the resulting
 * idToken (or null on cancellation/failure). Android uses an ActivityResultContract; iOS
 * delegates to a Swift-provided GoogleSignInBridge.
 */
@Composable
expect fun rememberGoogleSignInTrigger(onIdToken: (String?) -> Unit): () -> Unit
