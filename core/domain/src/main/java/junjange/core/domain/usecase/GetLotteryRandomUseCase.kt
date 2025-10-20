package junjange.core.domain.usecase

import junjange.core.domain.model.LotteryRandomNumbers
import junjange.core.domain.repository.LotteryRepository
import javax.inject.Inject

class GetLotteryRandomUseCase
    @Inject
    constructor(
        private val repository: LotteryRepository,
    ) {
        suspend operator fun invoke(): Result<LotteryRandomNumbers> = repository.getLotteryRandom()
    }
