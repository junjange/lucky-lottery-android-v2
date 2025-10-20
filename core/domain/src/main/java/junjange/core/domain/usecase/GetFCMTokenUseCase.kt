package junjange.core.domain.usecase

import junjange.core.domain.repository.FirebaseRepository
import javax.inject.Inject

class GetFCMTokenUseCase
    @Inject
    constructor(
        private val repository: FirebaseRepository,
    ) {
        suspend operator fun invoke(): Result<String> = repository.getToken()
    }
