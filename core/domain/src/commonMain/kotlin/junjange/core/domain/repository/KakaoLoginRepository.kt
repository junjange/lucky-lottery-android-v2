package junjange.core.domain.repository

import junjange.core.domain.model.KakaoAccessToken

interface KakaoLoginRepository {
    suspend fun login(): Result<KakaoAccessToken>
}
