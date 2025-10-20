package junjange.feature.randomnumber

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import junjange.core.designsystem.theme.LottoTheme
import junjange.core.domain.model.LottoType
import junjange.core.navigation.MainNavigator
import junjange.core.navigation.RandomNumberNavigator
import junjange.core.ui.base.BaseActivity
import javax.inject.Inject

@AndroidEntryPoint
class RandomNumberActivity : BaseActivity() {
    @Inject
    lateinit var navigator: RandomNumberNavigator

    private val viewModel: RandomNumberViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LottoTheme {
                RandomNumberScreen(
                    viewModel = viewModel,
                    navigateRandomNumberGeneration = ::startRandomNumberGenerationActivity,
                    onBack = ::finish,
                )
            }
        }
    }

    private fun startRandomNumberGenerationActivity(lottoType: LottoType) {
        navigator.startRandomNumberGenerationActivity(
            context = this@RandomNumberActivity,
            lottoType = lottoType,
        )
    }

    companion object {
        fun startActivity(context: Context) {
            val intent = Intent(context, RandomNumberActivity::class.java)
            context.startActivity(intent)
        }
    }
}
