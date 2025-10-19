package com.junjange.presentation.ui.withdrawal

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import junjange.core.designsystem.theme.LottoTheme
import junjange.core.domain.model.OauthProvider
import junjange.core.ui.base.BaseActivity

@AndroidEntryPoint
class WithdrawalActivity : BaseActivity() {
    private val viewModel: WithdrawViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            LottoTheme {
                WithdrawalScreen(
                    viewModel = viewModel,
                    navigateToSplash = ::navigateToSplash,
                    onBack = ::finish,
                )
            }
        }
    }

    private fun navigateToSplash() {}

    companion object {
        const val SIGN_IN_REQUEST_CODE = 1
        const val EXTRA_KEY_OAUTH_PROVIDER = "EXTRA_KEY_OAUTH_PROVIDER"

        fun startActivity(
            context: Context,
            oauthProvider: OauthProvider,
        ) {
            val intent =
                Intent(context, WithdrawalActivity::class.java)
                    .putExtra(EXTRA_KEY_OAUTH_PROVIDER, oauthProvider)
            context.startActivity(intent)
        }
    }
}
