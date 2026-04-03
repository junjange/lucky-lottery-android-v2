package junjange.core.domain.usecase

import junjange.core.domain.model.PensionLotteryGet
import junjange.core.domain.repository.PensionLotteryRepository

class GetPensionLotteryGetUseCase
    
    constructor(
        private val repository: PensionLotteryRepository,
    ) {
        suspend operator fun invoke(
            page: Int,
            size: Int,
        ): Result<PensionLotteryGet> =
            repository.getPensionLotteryGet(
                page = page,
                size = size,
            )
    }
