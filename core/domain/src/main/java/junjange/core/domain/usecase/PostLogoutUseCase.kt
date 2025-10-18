package junjange.core.domain.usecase

import junjange.core.domain.repository.CredentialRepository
import javax.inject.Inject

class PostLogoutUseCase
    @Inject
    constructor(
        private val repository: CredentialRepository,
    ) {
        suspend operator fun invoke(): Result<Unit> = repository.postLogout()
    }
