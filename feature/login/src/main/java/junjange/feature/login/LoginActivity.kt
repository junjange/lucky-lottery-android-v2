package junjange.feature.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import junjange.core.designsystem.theme.LottoTheme
import junjange.core.ui.base.BaseActivity

@AndroidEntryPoint
class LoginActivity : BaseActivity() {
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LottoTheme {
                LoginScreen(
                    viewModel = viewModel,
                    navigateToMain = ::startMainActivity,
                    navigateToRegister = ::startRegisterActivity,
                )
            }
        }
    }

    private fun startMainActivity() {
//        MainActivity.startActivity(this)
    }

    private fun startRegisterActivity(
        idToken: String,
        provider: String,
    ) {
//        RegisterActivity.startActivity(this, idToken, provider)
    }

    companion object {
        fun startActivity(context: Context) {
            val intent = Intent(context, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }
}
