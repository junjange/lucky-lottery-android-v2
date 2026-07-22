package junjange.core.domain.usecase

import junjange.core.domain.repository.LotteryRepository
import javax.inject.Inject

class DeleteAllLotteryUseCase
    @Inject
    constructor(
        private val repository: LotteryRepository,
    ) {
        suspend operator fun invoke(): Result<Unit> = repository.deleteAllLottery()
    }
