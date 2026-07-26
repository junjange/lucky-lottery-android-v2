package junjange.core.domain.usecase

import junjange.core.domain.model.PensionLotteryRandom
import junjange.core.domain.repository.PensionLotteryRepository

class GetPensionLotteryRandomUseCase
    
    constructor(
        private val repository: PensionLotteryRepository,
    ) {
        suspend operator fun invoke(): Result<PensionLotteryRandom> = repository.getPensionLotteryRandom()
    }
