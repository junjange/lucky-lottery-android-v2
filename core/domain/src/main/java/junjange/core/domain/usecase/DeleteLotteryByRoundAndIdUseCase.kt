package junjange.core.domain.usecase

import junjange.core.domain.repository.LotteryRepository

class DeleteLotteryByRoundAndIdUseCase
    
    constructor(
        private val repository: LotteryRepository,
    ) {
        suspend operator fun invoke(
            round: Int,
            id: Long,
        ): Result<Unit> = repository.deleteLotteryByRoundAndId(round = round, id = id)
    }
