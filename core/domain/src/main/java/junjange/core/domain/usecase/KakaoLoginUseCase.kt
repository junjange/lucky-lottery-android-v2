package junjange.core.domain.usecase

import junjange.core.domain.model.KakaoAccessToken
import junjange.core.domain.repository.KakaoLoginRepository
import javax.inject.Inject

class KakaoLoginUseCase
    @Inject
    constructor(
        private val kakaoLoginRepository: KakaoLoginRepository,
    ) {
        suspend operator fun invoke(): Result<KakaoAccessToken> = kakaoLoginRepository.login()
    }
