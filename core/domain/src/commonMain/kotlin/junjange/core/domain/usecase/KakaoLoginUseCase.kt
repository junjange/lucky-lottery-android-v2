package junjange.core.domain.usecase

import junjange.core.domain.model.KakaoAccessToken
import junjange.core.domain.repository.KakaoLoginRepository

class KakaoLoginUseCase
    
    constructor(
        private val kakaoLoginRepository: KakaoLoginRepository,
    ) {
        suspend operator fun invoke(): Result<KakaoAccessToken> = kakaoLoginRepository.login()
    }
