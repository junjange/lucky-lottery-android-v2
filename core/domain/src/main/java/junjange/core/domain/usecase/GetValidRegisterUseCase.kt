package junjange.core.domain.usecase

import junjange.core.domain.model.IsRegistered
import junjange.core.domain.repository.CredentialRepository

class GetValidRegisterUseCase
    
    constructor(
        private val repository: CredentialRepository,
    ) {
        suspend operator fun invoke(
            idToken: String,
            provider: String,
        ): Result<IsRegistered> =
            repository.getValidRegister(
                idToken = idToken,
                provider = provider,
            )
    }
