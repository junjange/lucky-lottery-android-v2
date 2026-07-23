package junjange.feature.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import junjange.core.firebase.bridge.GoogleSignInBridge
import org.koin.mp.KoinPlatform
import platform.UIKit.UIDevice

@Composable
actual fun rememberDeviceId(): String =
    remember { UIDevice.currentDevice.identifierForVendor?.UUIDString ?: "" }

@Composable
actual fun rememberGoogleSignInTrigger(onIdToken: (String?) -> Unit): () -> Unit {
    val bridge = remember { KoinPlatform.getKoin().getOrNull<GoogleSignInBridge>() }
    return {
        val signIn = bridge
        if (signIn != null) signIn.signIn(onIdToken) else onIdToken(null)
    }
}
