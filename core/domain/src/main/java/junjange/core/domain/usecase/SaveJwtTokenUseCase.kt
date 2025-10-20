package junjange.core.domain.usecase

import junjange.core.domain.model.JwtToken
import junjange.core.domain.repository.LocalRepository
import javax.inject.Inject

class SaveJwtTokenUseCase
    @Inject
    constructor(
        private val repository: LocalRepository,
    ) {
        suspend operator fun invoke(jwtToken: JwtToken) = repository.saveJwtToken(jwtToken = jwtToken)
    }
