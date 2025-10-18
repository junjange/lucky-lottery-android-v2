package com.junjange.presentation.ui.register

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.junjange.presentation.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import junjange.core.designsystem.theme.LottoTheme
import junjange.core.ui.base.BaseActivity

@AndroidEntryPoint
class RegisterActivity : BaseActivity() {
    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LottoTheme {
                RegisterScreen(
                    viewModel = viewModel,
                    navigateToMain = ::startMainActivity,
                    onBack = ::finish,
                )
            }
        }
    }

    private fun startMainActivity() {
        MainActivity.startActivity(this)
    }

    companion object {
        fun startActivity(
            context: Context,
            idToken: String,
            provider: String,
        ) {
            val intent =
                Intent(context, RegisterActivity::class.java)
                    .putExtra(EXTRA_KEY_ID_TOKEN, idToken)
                    .putExtra(EXTRA_KEY_PROVIDER, provider)
            context.startActivity(intent)
        }

        const val EXTRA_KEY_ID_TOKEN = "EXTRA_KEY_ID_TOKEN"
        const val EXTRA_KEY_PROVIDER = "EXTRA_KEY_PROVIDER"
    }
}
