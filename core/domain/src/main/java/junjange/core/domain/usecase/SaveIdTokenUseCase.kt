package junjange.core.domain.usecase

import junjange.core.domain.repository.LocalRepository
import javax.inject.Inject

class SaveIdTokenUseCase
    @Inject
    constructor(
        private val repository: LocalRepository,
    ) {
        suspend operator fun invoke(idToken: String) = repository.saveIdToken(idToken = idToken)
    }
