package com.junjange.presentation.ui.my

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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.junjange.presentation.R
import com.junjange.presentation.ui.my.MyEffect.NavigateToEditProfile
import com.junjange.presentation.ui.my.MyEffect.NavigateToUsageTerm
import junjange.core.designsystem.theme.LottoTheme
import junjange.core.designsystem.theme.White
import junjange.core.domain.model.OauthProvider
import junjange.core.ui.component.LottoButtonBar
import junjange.core.ui.component.LottoSimpleTopBar
import junjange.core.ui.component.LottoTextBar
import kotlinx.coroutines.flow.collectLatest

@Composable
fun MyScreen(
    viewModel: MyViewModel = hiltViewModel(),
    navigateToWithdrawal: (oauthProvider: OauthProvider) -> Unit,
    navigateToSplash: () -> Unit,
    navigateToEditProfile: (nickname: String, profilePath: String?) -> Unit,
    navigateToNotification: (lottoNotificationState: Boolean, pensionLottoNotificationState: Boolean) -> Unit,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

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
        topBar = { LottoSimpleTopBar(titleRes = R.string.my) },
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
                                Image(
                                    painter = painterResource(id = R.drawable.app_icon),
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
                                painter = painterResource(id = R.drawable.ic_profile_edit),
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
                    textRes = R.string.app_notification,
                    onClick = { viewModel.onClickedNotification() },
                )
                LottoTextBar(
                    textRes = R.string.version_info,
                    subtextRes = R.string.newest_version,
                )
                LottoButtonBar(
                    textRes = R.string.usage_term,
                    onClick = { viewModel.onClickedUsageTerm() },
                )
                LottoButtonBar(
                    textRes = R.string.sign_out,
                    onClick = { viewModel.onClickedSignOut() },
                )
                LottoButtonBar(
                    textRes = R.string.withdraw_lotto,
                    onClick = { viewModel.onClickedWithdrawal() },
                )
            }
        }
    }
}
