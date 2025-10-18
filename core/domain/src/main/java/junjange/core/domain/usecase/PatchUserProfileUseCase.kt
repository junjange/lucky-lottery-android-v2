package junjange.core.domain.usecase

import junjange.core.domain.repository.UserRepository
import javax.inject.Inject

class PatchUserProfileUseCase
    @Inject
    constructor(
        private val repository: UserRepository,
    ) {
        suspend operator fun invoke(
            profilePath: String?,
            nickname: String,
        ): Result<Unit> = repository.patchUserMyInfo(profilePath = profilePath, nickname = nickname)
    }
