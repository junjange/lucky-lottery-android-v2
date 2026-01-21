package junjange.core.domain.usecase

import junjange.core.domain.repository.LocalRepository

class SaveIdTokenUseCase
    
    constructor(
        private val repository: LocalRepository,
    ) {
        suspend operator fun invoke(idToken: String) = repository.saveIdToken(idToken = idToken)
    }
