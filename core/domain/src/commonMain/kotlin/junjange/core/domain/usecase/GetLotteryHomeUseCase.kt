package junjange.core.domain.usecase

import junjange.core.domain.model.LotteryNumbers
import junjange.core.domain.repository.WinningRepository

class GetLotteryHomeUseCase
    
    constructor(
        private val repository: WinningRepository,
    ) {
        suspend operator fun invoke(): Result<LotteryNumbers> = repository.getLotteryHome()
    }
