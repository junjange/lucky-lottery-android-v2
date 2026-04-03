package junjange.core.data.datasource

import junjange.core.data.model.local.JwtTokenEntity

interface LocalDataSource {
    suspend fun getJwtToken(): Result<JwtTokenEntity?>

    suspend fun saveJwtToken(jwtTokenEntity: JwtTokenEntity): Result<Unit>

    suspend fun saveIdToken(idToken: String): Result<Unit>

    suspend fun getIdToken(): Result<String?>

    suspend fun deleteLocalData(): Result<Unit>
}
