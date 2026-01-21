package junjange.core.domain.usecase

import junjange.core.domain.repository.LocalRepository

class DeleteLocalDataUseCase
    
    constructor(
        private val repository: LocalRepository,
    ) {
        suspend operator fun invoke(): Result<Unit> = repository.deleteLocalData()
    }
