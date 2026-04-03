package junjange.core.domain.usecase

import junjange.core.domain.repository.PensionLotteryRepository

class GetPensionLotteryRoundUseCase
    
    constructor(
        private val repository: PensionLotteryRepository,
    ) {
        suspend operator fun invoke(): Result<Int> = repository.getPensionLotteryRound()
    }
