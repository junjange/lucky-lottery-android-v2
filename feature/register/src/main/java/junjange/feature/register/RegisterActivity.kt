package junjange.feature.register

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import org.koin.androidx.viewmodel.ext.android.viewModel
// import androidx.activity.viewModels
import junjange.core.designsystem.theme.LottoTheme
import junjange.core.navigation.RegisterNavigator
import junjange.core.ui.base.BaseActivity
import org.koin.android.ext.android.inject


class RegisterActivity : BaseActivity() {

    private val navigator: RegisterNavigator by inject()

    private val viewModel: RegisterViewModel by viewModel()

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
        navigator.startMainActivity(context = this@RegisterActivity)
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
