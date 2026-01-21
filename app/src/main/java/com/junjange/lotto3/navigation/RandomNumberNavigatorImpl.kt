package com.junjange.lotto3.navigation

import android.content.Context
import junjange.core.domain.model.LottoType
import junjange.core.navigation.RandomNumberNavigator
import junjange.feature.randomnumbergeneration.RandomNumberGenerationActivity

class RandomNumberNavigatorImpl : RandomNumberNavigator {
        override fun startRandomNumberGenerationActivity(
            context: Context,
            lottoType: LottoType,
        ) {
            RandomNumberGenerationActivity.startActivity(
                context = context,
                lottoType = lottoType,
            )
        }
    }
