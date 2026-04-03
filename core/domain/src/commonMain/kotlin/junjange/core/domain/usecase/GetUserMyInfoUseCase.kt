package junjange.core.domain.usecase

import junjange.core.domain.model.UserMyInfo
import junjange.core.domain.repository.UserRepository

class GetUserMyInfoUseCase
    
    constructor(
        private val repository: UserRepository,
    ) {
        suspend operator fun invoke(): Result<UserMyInfo> = repository.getUserMyInfo()
    }
