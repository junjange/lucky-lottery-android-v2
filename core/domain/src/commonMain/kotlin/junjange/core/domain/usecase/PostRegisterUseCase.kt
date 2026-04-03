package junjange.core.domain.usecase

import junjange.core.domain.model.JwtToken
import junjange.core.domain.repository.CredentialRepository

class PostRegisterUseCase
    
    constructor(
        private val repository: CredentialRepository,
    ) {
        suspend operator fun invoke(
            idToken: String,
            provider: String,
            nickName: String,
        ): Result<JwtToken> =
            repository.postRegister(
                idToken = idToken,
                provider = provider,
                nickName = nickName,
            )
    }
