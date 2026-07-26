package junjange.core.domain.usecase

import junjange.core.domain.model.PensionLotteryGetContent
import junjange.core.domain.repository.PensionLotteryRepository

class LoadPensionLotteryRoundsUseCase
    
    constructor(
        private val repository: PensionLotteryRepository,
    ) {
        suspend operator fun invoke(
            page: Int,
            size: Int,
        ): Result<List<PensionLotteryGetContent>> = repository.loadPensionLotteryRounds(page = page, size = size)
    }
