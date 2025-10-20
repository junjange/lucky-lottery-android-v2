package junjange.core.local.datasource

import android.content.SharedPreferences
import androidx.core.content.edit
import junjange.core.data.datasource.LocalDataSource
import junjange.core.data.model.local.JwtTokenEntity
import javax.inject.Inject

internal class LocalDataSourceImpl
    @Inject
    constructor(
        private val sharedPreferences: SharedPreferences,
    ) : LocalDataSource {
        override suspend fun getJwtToken(): Result<JwtTokenEntity?> =
            runCatching {
                val accessToken = sharedPreferences.getString(KEY_ACCESS_TOKEN, null)
                val refreshToken = sharedPreferences.getString(KEY_REFRESH_TOKEN, null)
                if (accessToken == null || refreshToken == null) {
                    return@runCatching null
                }
                JwtTokenEntity(accessToken = accessToken, refreshToken = refreshToken)
            }

        override suspend fun saveJwtToken(jwtTokenEntity: JwtTokenEntity): Result<Unit> =
            runCatching {
                sharedPreferences.edit {
                    putString(KEY_ACCESS_TOKEN, jwtTokenEntity.accessToken)
                    putString(KEY_REFRESH_TOKEN, jwtTokenEntity.refreshToken)
                }
            }

        override suspend fun saveIdToken(idToken: String): Result<Unit> =
            runCatching {
                sharedPreferences.edit {
                    putString(KEY_ID_TOKEN, idToken)
                }
            }

        override suspend fun getIdToken(): Result<String?> =
            runCatching {
                val idToken = sharedPreferences.getString(KEY_ID_TOKEN, null) ?: return@runCatching null
                idToken
            }

        override suspend fun deleteLocalData(): Result<Unit> =
            runCatching {
                sharedPreferences.edit {
                    clear()
                    commit()
                }
            }

        companion object {
            private const val KEY_ACCESS_TOKEN = "KEY_ACCESS_TOKEN"
            private const val KEY_REFRESH_TOKEN = "KEY_REFRESH_TOKEN"
            private const val KEY_ID_TOKEN = "KEY_ID_TOKEN"
        }
    }
