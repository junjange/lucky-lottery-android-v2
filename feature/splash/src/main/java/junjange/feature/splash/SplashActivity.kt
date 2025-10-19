package junjange.feature.splash

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import junjange.core.designsystem.theme.LottoTheme
import junjange.core.navigation.SplashNavigator
import junjange.core.ui.base.BaseActivity
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : BaseActivity() {
    @Inject
    lateinit var navigator: SplashNavigator

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
        navigator.startMainActivity(context = this@SplashActivity)
    }

    private fun startLoginActivity() {
        navigator.startLoginActivity(context = this@SplashActivity)
    }
}
