package junjange.core.domain.usecase

import junjange.core.domain.repository.UserRepository

class PatchUserProfileUseCase
    
    constructor(
        private val repository: UserRepository,
    ) {
        suspend operator fun invoke(
            profilePath: String?,
            nickname: String,
        ): Result<Unit> = repository.patchUserMyInfo(profilePath = profilePath, nickname = nickname)
    }
