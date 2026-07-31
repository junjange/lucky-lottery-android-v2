package junjange.feature.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import junjange.core.designsystem.theme.LottoSpacing
import junjange.core.ui.component.EdgeToEdgeLayout
import junjange.feature.splash.resources.Res
import junjange.feature.splash.resources.app_logo
import junjange.feature.splash.resources.splash_logo_description
import junjange.feature.splash.resources.splash_tagline
import kotlinx.coroutines.flow.collectLatest
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

/** 스플래시 로고 크기. iOS 런치스크린의 로고와 같은 크기로 맞춰 전환이 이어져 보이게 한다. */
private val LogoSize = 120.dp

/**
 * 스플래시.
 *
 * 예전에는 로고가 주석으로 막혀 있어(안드로이드 전용 `R.drawable`을 쓰던 자리) 글자만 떴다.
 * 로고를 공용 리소스로 옮겨 두 플랫폼에서 같이 보이게 한다.
 */
@Composable
fun SplashScreen(
    viewModel: SplashViewModel,
    navigateToMain: () -> Unit,
) {
    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                SplashContract.Effect.NavigateToMain -> navigateToMain()
            }
        }
    }

    EdgeToEdgeLayout(applySystemBarsPadding = false) {
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
        ) {
            Column(
                modifier = Modifier.align(Alignment.Center),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(
                    modifier = Modifier.size(LogoSize),
                    painter = painterResource(Res.drawable.app_logo),
                    contentDescription = stringResource(Res.string.splash_logo_description),
                )

                Spacer(modifier = Modifier.height(LottoSpacing.base))

                Text(
                    text = stringResource(Res.string.splash_tagline),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}
