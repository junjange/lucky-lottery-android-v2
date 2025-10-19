package com.junjange.presentation.ui.withdrawal

import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.junjange.presentation.R
import com.junjange.presentation.ui.dialog.WithdrawalDialog
import com.junjange.presentation.ui.login.GoogleSignInContract
import junjange.core.designsystem.theme.LottoTheme
import junjange.core.domain.model.OauthProvider
import junjange.core.ui.component.LottoButtonBar
import junjange.core.ui.component.LottoSimpleTopBar
import kotlinx.coroutines.flow.collectLatest

@Composable
fun WithdrawalScreen(
    viewModel: WithdrawViewModel,
    navigateToSplash: () -> Unit,
    onBack: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val authResultLauncher =
        rememberLauncherForActivityResult(
            contract = GoogleSignInContract(),
            onResult = { viewModel.googleLogin(result = it) },
        )

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is WithdrawalEffect.AddStep -> {
                    viewModel.addStep(step = 1)
                    viewModel.onClickedDialog(isWithdrawalDialogShowing = false)
                }
            }
        }
    }

    BackHandler {
        if (uiState.step == 1) {
            onBack()
        } else if (uiState.step < 2) {
            viewModel.addStep(step = -1)
        }
    }

    if (uiState.isWithdrawalDialogShowing) {
        WithdrawalDialog(
            onDismiss = { viewModel.onClickedDialog(isWithdrawalDialogShowing = false) },
            okClick = {
                when (uiState.oauthProvider) {
                    OauthProvider.KAKAO -> viewModel.deleteMe()
                    OauthProvider.GOOGLE -> authResultLauncher.launch(WithdrawalActivity.SIGN_IN_REQUEST_CODE)
                }
            },
        )
    }

    Scaffold(
        topBar = {
            if (uiState.step < 2) {
                LottoSimpleTopBar(
                    onBack = {
                        if (uiState.step == 1) {
                            onBack()
                        } else {
                            viewModel.addStep(step = -1)
                        }
                    },
                    backIconRes = R.drawable.ic_back,
                    titleRes = R.string.withdrawal,
                )
            }
        },
    ) { innerPadding ->
        when (uiState.step) {
            1 -> {
                Column(
                    modifier = Modifier.padding(innerPadding),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Text(
                        text = stringResource(id = R.string.withdrawal_question),
                        style = LottoTheme.typography.headline1,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    WithdrawalOption.entries.forEach {
                        LottoButtonBar(
                            textRes = it.textRes,
                            onClick = { viewModel.onClickedDialog(isWithdrawalDialogShowing = true) },
                        )
                    }
                }
            }

            2 -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier =
                        Modifier
                            .padding(top = 80.dp, bottom = 40.dp)
                            .padding(horizontal = 16.dp),
                ) {
                    Text(
                        text = stringResource(id = R.string.thanks_for_playing),
                        style = LottoTheme.typography.headline1,
                        modifier = Modifier.padding(horizontal = 36.dp),
                    )
                    Spacer(modifier = Modifier.height(18.dp))
                    Text(
                        text = stringResource(id = R.string.thanks_for_playing_description),
                        style = LottoTheme.typography.body3,
                        modifier = Modifier.padding(horizontal = 36.dp),
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Button(
                        onClick = navigateToSplash,
                        colors =
                            ButtonDefaults.buttonColors(
                                containerColor = LottoTheme.colors.lottoBlack,
                                contentColor = LottoTheme.colors.gray50,
                            ),
                        shape = RoundedCornerShape(16.dp),
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                    ) {
                        Text(
                            text = stringResource(id = R.string.back_to_title),
                            style = LottoTheme.typography.body1,
                            color = LottoTheme.colors.lottoWhite,
                        )
                    }
                }
            }

            else -> {}
        }
    }
}
