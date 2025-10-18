package junjange.core.domain.usecase

import junjange.core.domain.repository.CredentialRepository
import javax.inject.Inject

class DeleteMeUseCase
    @Inject
    constructor(
        private val repository: CredentialRepository,
    ) {
        suspend operator fun invoke(oauthAccessToken: String?): Result<Unit> = repository.deleteMe(oauthAccessToken = oauthAccessToken)
    }
