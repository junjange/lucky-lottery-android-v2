package junjange.core.domain.usecase

import junjange.core.domain.model.JwtToken
import junjange.core.domain.repository.CredentialRepository
import javax.inject.Inject

class PostLoginUseCase
    @Inject
    constructor(
        private val repository: CredentialRepository,
    ) {
        suspend operator fun invoke(
            idToken: String,
            provider: String,
        ): Result<JwtToken> =
            repository.postLogin(
                idToken = idToken,
                provider = provider,
            )
    }
