package junjange.feature.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import junjange.core.ui.component.EdgeToEdgeLayout
import junjange.feature.login.resources.*
import junjange.feature.login.resources.ic_clover
import junjange.feature.login.resources.ic_google_login
import junjange.feature.login.resources.ic_kakao_login
import kotlinx.coroutines.flow.collectLatest

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    navigateToMain: () -> Unit,
    navigateToRegister: (idToken: String, provider: String) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    val deviceId = rememberDeviceId()

    val launchGoogleSignIn =
        rememberGoogleSignInTrigger { idToken ->
            viewModel.googleLogin(idToken = idToken, deviceId = deviceId)
        }

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is LoginEffect.NavigateToMain -> navigateToMain()
                is LoginEffect.NavigateToRegister ->
                    navigateToRegister(
                        effect.idToken,
                        effect.provider,
                    )
            }
        }
    }

    EdgeToEdgeLayout {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .align(Alignment.Center),
                painter = painterResource(Res.drawable.ic_clover),
                contentDescription = null,
            )
            Column(
                modifier =
                    Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 24.dp),
            ) {
                LoginButton(
                    iconRes = Res.drawable.ic_kakao_login,
                    onClick = { viewModel.kakaoLogin(deviceId = deviceId) },
                )
                Spacer(modifier = Modifier.height(8.dp))
                LoginButton(
                    iconRes = Res.drawable.ic_google_login,
                    onClick = { launchGoogleSignIn() },
                )
            }
        }
    }
}

@Composable
fun LoginButton(
    iconRes: DrawableResource,
    onClick: () -> Unit,
) {
    Image(
        painter = painterResource(iconRes),
        contentDescription = null,
        modifier =
            Modifier
                .fillMaxWidth()
                .aspectRatio(328 / 52f)
                .padding(horizontal = 16.dp)
                .clickable { onClick() },
    )
}
