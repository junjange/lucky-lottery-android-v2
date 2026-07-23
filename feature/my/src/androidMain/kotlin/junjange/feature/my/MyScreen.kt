package junjange.feature.my

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import org.jetbrains.compose.resources.painterResource
import androidx.compose.ui.res.painterResource as androidPainterResource
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import coil.compose.AsyncImage
import junjange.core.designsystem.theme.LottoTheme
import junjange.core.designsystem.theme.White
import junjange.core.domain.model.OauthProvider
import junjange.core.ui.component.LottoButtonBar
import junjange.core.ui.component.LottoSimpleTopBar
import junjange.core.ui.component.LottoTextBar
import junjange.feature.my.MyEffect.NavigateToEditProfile
import junjange.feature.my.MyEffect.NavigateToUsageTerm
import junjange.feature.my.resources.*
import junjange.feature.my.resources.app_notification
import junjange.feature.my.resources.ic_profile_edit
import junjange.feature.my.resources.my
import junjange.feature.my.resources.newest_version
import junjange.feature.my.resources.sign_out
import junjange.feature.my.resources.usage_term
import junjange.feature.my.resources.version_info
import junjange.feature.my.resources.withdraw_lotto
import kotlinx.coroutines.flow.collectLatest

@Composable
fun MyScreen(
    viewModel: MyViewModel = koinViewModel(),
    navigateToWithdrawal: (oauthProvider: OauthProvider) -> Unit,
    navigateToSplash: () -> Unit,
    navigateToEditProfile: (nickname: String, profilePath: String?) -> Unit,
    navigateToNotification: (lottoNotificationState: Boolean, pensionLottoNotificationState: Boolean) -> Unit,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
) {
    val uiState by viewModel.uiState.collectAsState()

    val context = LocalContext.current

    DisposableEffect(lifecycleOwner) {
        val observer =
            LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_RESUME) {
                    viewModel.getUserMyInfo()
                }
            }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    val usageTermUri = "https://fre2-dom.tistory.com/7"
    val intent =
        Intent(
            Intent.ACTION_VIEW,
            Uri.parse(usageTermUri),
        )

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is NavigateToEditProfile ->
                    navigateToEditProfile(
                        effect.nickname,
                        effect.profilePath,
                    )

                is NavigateToUsageTerm -> context.startActivity(intent)
                is MyEffect.NavigateToSplash -> navigateToSplash()
                is MyEffect.NavigateToWithdrawal -> navigateToWithdrawal(effect.oauthProvider)
                is MyEffect.NavigateToNotification ->
                    navigateToNotification(
                        effect.lottoNotificationState,
                        effect.pensionLottoNotificationState,
                    )
            }
        }
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = { LottoSimpleTopBar(titleRes = Res.string.my) },
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            Box {
                Surface(
                    modifier =
                        Modifier
                            .padding(horizontal = 16.dp, vertical = 24.dp)
                            .fillMaxWidth(),
                    color = LottoTheme.colors.lottoBlack,
                    shape = RoundedCornerShape(32.dp),
                ) {
                    Row(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Box {
                            uiState.profilePath?.let { profilePath ->
                                AsyncImage(
                                    model = profilePath,
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier =
                                        Modifier
                                            .size(120.dp)
                                            .clip(RoundedCornerShape(32.dp))
                                            .border(
                                                width = 1.dp,
                                                color = White,
                                                shape = RoundedCornerShape(32.dp),
                                            ),
                                )
                            } ?: run {
                                // TODO: Replace app_icon with compose multiplatform resource
                                Image(
                                    painter = androidPainterResource(R.drawable.app_icon),
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier =
                                        Modifier
                                            .size(120.dp)
                                            .clip(RoundedCornerShape(32.dp))
                                            .border(
                                                width = 1.dp,
                                                color = White,
                                                shape = RoundedCornerShape(32.dp),
                                            ).background(color = White),
                                )
                            }

                            Image(
                                painter = painterResource(Res.drawable.ic_profile_edit),
                                contentDescription = null,
                                modifier =
                                    Modifier
                                        .size(32.dp)
                                        .offset(x = 4.dp, y = 4.dp)
                                        .align(Alignment.BottomEnd)
                                        .clickable { viewModel.onClickedEditProfile() },
                            )
                        }
                        Spacer(modifier = Modifier.width(32.dp))
                        Column {
                            Text(
                                text = uiState.nickname,
                                style = LottoTheme.typography.headline1,
                            )
                            Text(
                                text = uiState.oauthProvider.displayName,
                                style = LottoTheme.typography.body2,
                            )
                        }
                    }
                }
            }
            Divider(thickness = 8.dp, color = LottoTheme.colors.gray200)
            Column(
                modifier = Modifier.padding(vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                LottoButtonBar(
                    textRes = Res.string.app_notification,
                    onClick = { viewModel.onClickedNotification() },
                )
                LottoTextBar(
                    text = stringResource(Res.string.version_info),
                    subtext = stringResource(Res.string.newest_version),
                )
                LottoButtonBar(
                    textRes = Res.string.usage_term,
                    onClick = { viewModel.onClickedUsageTerm() },
                )
                LottoButtonBar(
                    textRes = Res.string.sign_out,
                    onClick = { viewModel.onClickedSignOut() },
                )
                LottoButtonBar(
                    textRes = Res.string.withdraw_lotto,
                    onClick = { viewModel.onClickedWithdrawal() },
                )
            }
        }
    }
}
