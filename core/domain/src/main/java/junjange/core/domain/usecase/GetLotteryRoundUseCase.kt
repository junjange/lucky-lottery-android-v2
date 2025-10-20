package junjange.core.domain.usecase

import junjange.core.domain.repository.LotteryRepository
import javax.inject.Inject

class GetLotteryRoundUseCase
    @Inject
    constructor(
        private val repository: LotteryRepository,
    ) {
        suspend operator fun invoke(): Result<Int> = repository.getLotteryRound()
    }
