package junjange.core.domain.usecase

import junjange.core.domain.model.JwtToken
import junjange.core.domain.repository.LocalRepository

class GetJwtTokenUseCase
    
    constructor(
        private val repository: LocalRepository,
    ) {
        suspend operator fun invoke(): Result<JwtToken?> = repository.getJwtToken()
    }
