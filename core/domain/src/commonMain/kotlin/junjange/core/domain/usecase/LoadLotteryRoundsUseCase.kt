package junjange.core.domain.usecase

import junjange.core.domain.model.LotteryGetContent
import junjange.core.domain.repository.LotteryRepository

class LoadLotteryRoundsUseCase
    
    constructor(
        private val repository: LotteryRepository,
    ) {
        suspend operator fun invoke(
            page: Int,
            size: Int,
        ): Result<List<LotteryGetContent>> = repository.loadLotteryRounds(page = page, size = size)
    }
