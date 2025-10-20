package junjange.feature.randomnumbergeneration

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import junjange.core.designsystem.theme.LottoTheme
import junjange.core.domain.model.LottoType
import junjange.core.navigation.RandomNumberGenerationNavigator
import junjange.core.navigation.RandomNumberNavigator
import junjange.core.ui.base.BaseActivity
import javax.inject.Inject

@AndroidEntryPoint
class RandomNumberGenerationActivity : BaseActivity() {
    @Inject
    lateinit var navigator: RandomNumberGenerationNavigator

    private val viewModel: RandomNumberGenerationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LottoTheme {
                RandomNumberGenerationScreen(
                    viewModel = viewModel,
                    navigateToMain = { initialPage ->
                        navigator.startMainActivity(
                            context = this@RandomNumberGenerationActivity,
                            initialPage = initialPage,
                        )
                        finish()
                    },
                    onBack = ::finish,
                )
            }
        }
    }

    companion object {
        fun startActivity(
            context: Context,
            lottoType: LottoType,
        ) {
            val intent = Intent(context, RandomNumberGenerationActivity::class.java)
            intent.putExtra(LOTTO_TYPE, lottoType.name)
            context.startActivity(intent)
        }

        const val LOTTO_TYPE = "lottoType"
    }
}
