package junjange.core.navigation

import android.content.Context

interface RandomNumberGenerationNavigator {
    fun startMainActivity(
        context: Context,
        initialPage: String,
    )
}
