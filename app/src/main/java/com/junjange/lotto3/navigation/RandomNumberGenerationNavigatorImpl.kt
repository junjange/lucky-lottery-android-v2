package com.junjange.lotto3.navigation

import android.content.Context
import junjange.core.domain.model.LottoType
import junjange.core.navigation.RandomNumberGenerationNavigator
import junjange.core.navigation.RandomNumberNavigator
import junjange.feature.main.MainActivity
import junjange.feature.randomnumbergeneration.RandomNumberGenerationActivity
import javax.inject.Inject

class RandomNumberGenerationNavigatorImpl
    @Inject
    constructor() : RandomNumberGenerationNavigator {
        override fun startMainActivity(
            context: Context,
            initialPage: String,
        ) {
            MainActivity.startActivity(
                context = context,
                initialPage = initialPage,
            )
        }
    }
