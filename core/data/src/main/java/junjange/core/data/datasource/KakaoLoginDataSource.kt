package junjange.core.data.datasource

import junjange.core.data.model.local.KakaoAccessTokenEntity

interface KakaoLoginDataSource {
    suspend fun login(): Result<KakaoAccessTokenEntity>
}
