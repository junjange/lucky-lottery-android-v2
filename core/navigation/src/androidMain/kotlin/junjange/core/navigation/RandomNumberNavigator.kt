package junjange.core.navigation

import android.content.Context
import junjange.core.domain.model.LottoType

interface RandomNumberNavigator {
    fun startRandomNumberGenerationActivity(
        context: Context,
        lottoType: LottoType,
    )
}
