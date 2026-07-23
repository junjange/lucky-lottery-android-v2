package junjange.core.local.datasource

import com.russhwolf.settings.Settings
import junjange.core.data.datasource.LocalDataSource
import junjange.core.data.model.local.JwtTokenEntity

internal class SettingsLocalDataSource(
    private val settings: Settings,
) : LocalDataSource {
    override suspend fun getJwtToken(): Result<JwtTokenEntity?> =
        runCatching {
            val accessToken = settings.getStringOrNull(KEY_ACCESS_TOKEN)
            val refreshToken = settings.getStringOrNull(KEY_REFRESH_TOKEN)
            if (accessToken == null || refreshToken == null) {
                return@runCatching null
            }
            JwtTokenEntity(accessToken = accessToken, refreshToken = refreshToken)
        }

    override suspend fun saveJwtToken(jwtTokenEntity: JwtTokenEntity): Result<Unit> =
        runCatching {
            jwtTokenEntity.accessToken?.let { settings.putString(KEY_ACCESS_TOKEN, it) }
            jwtTokenEntity.refreshToken?.let { settings.putString(KEY_REFRESH_TOKEN, it) }
        }

    override suspend fun saveIdToken(idToken: String): Result<Unit> =
        runCatching { settings.putString(KEY_ID_TOKEN, idToken) }

    override suspend fun getIdToken(): Result<String?> =
        runCatching { settings.getStringOrNull(KEY_ID_TOKEN) }

    override suspend fun deleteLocalData(): Result<Unit> =
        runCatching { settings.clear() }

    companion object {
        private const val KEY_ACCESS_TOKEN = "KEY_ACCESS_TOKEN"
        private const val KEY_REFRESH_TOKEN = "KEY_REFRESH_TOKEN"
        private const val KEY_ID_TOKEN = "KEY_ID_TOKEN"
    }
}
