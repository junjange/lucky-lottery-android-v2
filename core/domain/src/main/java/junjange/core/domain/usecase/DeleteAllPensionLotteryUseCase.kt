package junjange.core.domain.usecase

import junjange.core.domain.repository.PensionLotteryRepository
import javax.inject.Inject

class DeleteAllPensionLotteryUseCase
    @Inject
    constructor(
        private val repository: PensionLotteryRepository,
    ) {
        suspend operator fun invoke(): Result<Unit> = repository.deleteAllPensionLottery()
    }
