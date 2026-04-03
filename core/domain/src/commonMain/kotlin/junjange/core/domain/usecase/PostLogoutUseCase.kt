package junjange.core.domain.usecase

import junjange.core.domain.repository.CredentialRepository

class PostLogoutUseCase
    
    constructor(
        private val repository: CredentialRepository,
    ) {
        suspend operator fun invoke(): Result<Unit> = repository.postLogout()
    }
