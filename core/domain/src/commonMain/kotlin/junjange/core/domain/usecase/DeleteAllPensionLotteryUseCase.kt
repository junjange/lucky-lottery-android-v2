package junjange.core.domain.usecase

import junjange.core.domain.repository.PensionLotteryRepository

class DeleteAllPensionLotteryUseCase
    constructor(
        private val repository: PensionLotteryRepository,
    ) {
        suspend operator fun invoke(): Result<Unit> = repository.deleteAllPensionLottery()
    }
