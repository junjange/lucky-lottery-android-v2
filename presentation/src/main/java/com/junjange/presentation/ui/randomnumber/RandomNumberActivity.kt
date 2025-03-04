package com.junjange.presentation.ui.randomnumber

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.junjange.presentation.base.BaseActivity
import com.junjange.presentation.component.LottoType
import com.junjange.presentation.theme.LottoTheme
import com.junjange.presentation.ui.randomnumbergeneration.RandomNumberGenerationActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RandomNumberActivity : BaseActivity() {
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
        RandomNumberGenerationActivity.startActivity(
            context = this,
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
