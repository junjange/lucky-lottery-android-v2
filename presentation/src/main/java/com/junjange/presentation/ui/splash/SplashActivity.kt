package com.junjange.presentation.ui.splash

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.junjange.presentation.base.BaseActivity
import com.junjange.presentation.theme.LottoTheme
import com.junjange.presentation.ui.login.LoginActivity
import com.junjange.presentation.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : BaseActivity() {
    private val viewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LottoTheme {
                SplashScreen(
                    viewModel = viewModel,
                    navigateToMain = ::startMainActivity,
                    navigateToLogin = ::startLoginActivity,
                )
            }
        }
    }

    private fun startMainActivity() {
        MainActivity.startActivity(this)
    }

    private fun startLoginActivity() {
        LoginActivity.startActivity(this)
    }
}
