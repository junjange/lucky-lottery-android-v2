package junjange.core.domain.usecase

import junjange.core.domain.repository.LotteryRepository

class DeleteAllLotteryUseCase
    constructor(
        private val repository: LotteryRepository,
    ) {
        suspend operator fun invoke(): Result<Unit> = repository.deleteAllLottery()
    }
