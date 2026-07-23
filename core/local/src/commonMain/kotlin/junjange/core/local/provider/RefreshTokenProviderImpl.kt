package junjange.core.local.provider

import com.russhwolf.settings.Settings
import junjange.core.data.provider.RefreshTokenProvider

internal class RefreshTokenProviderImpl(
    private val settings: Settings,
) : RefreshTokenProvider {
    override var value: String
        get() = settings.getStringOrNull(KEY_REFRESH_TOKEN) ?: ""
        set(value) {
            settings.putString(KEY_REFRESH_TOKEN, value)
        }

    companion object {
        private const val KEY_REFRESH_TOKEN = "KEY_REFRESH_TOKEN"
    }
}
