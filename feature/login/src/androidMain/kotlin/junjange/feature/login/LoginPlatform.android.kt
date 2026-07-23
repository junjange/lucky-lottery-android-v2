package junjange.feature.login

import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.common.api.ApiException
import junjange.feature.google.GoogleSignInContract

@Composable
actual fun rememberDeviceId(): String {
    val context = LocalContext.current
    return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID) ?: ""
}

@Composable
actual fun rememberGoogleSignInTrigger(onIdToken: (String?) -> Unit): () -> Unit {
    val launcher =
        rememberLauncherForActivityResult(
            contract = GoogleSignInContract(),
            onResult = { task ->
                val idToken =
                    try {
                        task?.getResult(ApiException::class.java)?.idToken
                    } catch (e: ApiException) {
                        null
                    }
                onIdToken(idToken)
            },
        )
    return { launcher.launch(SIGN_IN_REQUEST_CODE) }
}

private const val SIGN_IN_REQUEST_CODE = 1
