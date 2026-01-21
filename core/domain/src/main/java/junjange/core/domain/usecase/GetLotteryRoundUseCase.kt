package junjange.core.domain.usecase

import junjange.core.domain.repository.LotteryRepository

class GetLotteryRoundUseCase
    
    constructor(
        private val repository: LotteryRepository,
    ) {
        suspend operator fun invoke(): Result<Int> = repository.getLotteryRound()
    }
