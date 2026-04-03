package junjange.core.data.repository

import junjange.core.data.datasource.KakaoLoginDataSource
import junjange.core.data.mapper.toDomain
import junjange.core.domain.model.KakaoAccessToken
import junjange.core.domain.repository.KakaoLoginRepository

internal class KakaoLoginRepositoryImpl
    
    constructor(
        private val kakaoLoginDataSource: KakaoLoginDataSource,
    ) : KakaoLoginRepository {
        override suspend fun login(): Result<KakaoAccessToken> = kakaoLoginDataSource.login().mapCatching { it.toDomain() }
    }
