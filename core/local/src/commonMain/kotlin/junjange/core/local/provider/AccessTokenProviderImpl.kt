package junjange.core.local.provider

import com.russhwolf.settings.Settings
import junjange.core.data.provider.AccessTokenProvider

internal class AccessTokenProviderImpl(
    private val settings: Settings,
) : AccessTokenProvider {
    override var value: String
        get() = settings.getStringOrNull(KEY_ACCESS_TOKEN) ?: ""
        set(value) {
            settings.putString(KEY_ACCESS_TOKEN, value)
        }

    companion object {
        private const val KEY_ACCESS_TOKEN = "KEY_ACCESS_TOKEN"
    }
}
