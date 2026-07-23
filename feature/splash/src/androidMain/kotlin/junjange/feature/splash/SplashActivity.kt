package junjange.feature.splash

import android.os.Bundle
import androidx.activity.compose.setContent
import junjange.core.designsystem.theme.LottoTheme
import junjange.core.navigation.SplashNavigator
import junjange.core.ui.base.BaseActivity
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashActivity : BaseActivity() {
    private val navigator: SplashNavigator by inject()

    private val viewModel: SplashViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LottoTheme {
                SplashScreen(
                    viewModel = viewModel,
                    navigateToMain = ::startMainActivity,
                )
            }
        }
    }

    private fun startMainActivity() {
        navigator.startMainActivity(context = this@SplashActivity)
    }
}
