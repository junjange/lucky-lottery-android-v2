package junjange.core.domain.usecase

import junjange.core.domain.model.PensionLotteryRandom
import junjange.core.domain.repository.PensionLotteryRepository
import javax.inject.Inject

class GetPensionLotteryRandomUseCase
    @Inject
    constructor(
        private val repository: PensionLotteryRepository,
    ) {
        suspend operator fun invoke(): Result<PensionLotteryRandom> = repository.getPensionLotteryRandom()
    }
