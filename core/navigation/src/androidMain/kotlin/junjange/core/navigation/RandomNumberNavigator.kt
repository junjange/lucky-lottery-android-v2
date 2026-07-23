package junjange.core.navigation

import android.content.Context
import junjange.core.domain.model.LottoType
import junjange.core.domain.model.OauthProvider

interface RandomNumberNavigator {
    fun startRandomNumberGenerationActivity(
        context: Context,
        lottoType: LottoType,
    )
}
