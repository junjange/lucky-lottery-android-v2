package junjange.core.domain.usecase

import junjange.core.domain.repository.FirebaseRepository

class GetFCMTokenUseCase
    
    constructor(
        private val repository: FirebaseRepository,
    ) {
        suspend operator fun invoke(): Result<String> = repository.getToken()
    }
