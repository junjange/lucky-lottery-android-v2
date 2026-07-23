package junjange.feature.randomnumber

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import org.koin.androidx.viewmodel.ext.android.viewModel
// import androidx.activity.viewModels
import junjange.core.designsystem.theme.LottoTheme
import junjange.core.domain.model.LottoType
import junjange.core.navigation.MainNavigator
import junjange.core.navigation.RandomNumberNavigator
import junjange.core.ui.base.BaseActivity
import org.koin.android.ext.android.inject


class RandomNumberActivity : BaseActivity() {

    private val navigator: RandomNumberNavigator by inject()

    private val viewModel: RandomNumberViewModel by viewModel()

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
