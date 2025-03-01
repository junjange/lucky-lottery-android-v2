package com.junjange.presentation.ui.randomnumbergeneration

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.junjange.presentation.base.BaseActivity
import com.junjange.presentation.component.LottoType
import com.junjange.presentation.ui.theme.LottoTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RandomNumberGenerationActivity : BaseActivity() {
    private val viewModel: RandomNumberGenerationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LottoTheme {
                RandomNumberGenerationScreen(
                    viewModel = viewModel,
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
