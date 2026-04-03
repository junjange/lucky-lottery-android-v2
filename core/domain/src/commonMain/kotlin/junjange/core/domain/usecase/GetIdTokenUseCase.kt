package junjange.core.domain.usecase

import junjange.core.domain.repository.LocalRepository

class GetIdTokenUseCase
    
    constructor(
        private val repository: LocalRepository,
    ) {
        suspend operator fun invoke(): Result<String?> = repository.getIdToken()
    }
