package junjange.core.domain.usecase

import junjange.core.domain.model.LotteryRandomNumbers
import junjange.core.domain.repository.LotteryRepository

class GetLotteryRandomUseCase
    
    constructor(
        private val repository: LotteryRepository,
    ) {
        suspend operator fun invoke(): Result<LotteryRandomNumbers> = repository.getLotteryRandom()
    }
