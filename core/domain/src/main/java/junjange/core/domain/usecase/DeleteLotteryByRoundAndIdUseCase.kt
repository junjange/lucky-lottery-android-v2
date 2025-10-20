package junjange.core.domain.usecase

import junjange.core.domain.repository.LotteryRepository
import javax.inject.Inject

class DeleteLotteryByRoundAndIdUseCase
    @Inject
    constructor(
        private val repository: LotteryRepository,
    ) {
        suspend operator fun invoke(
            round: Int,
            id: Long,
        ): Result<Unit> = repository.deleteLotteryByRoundAndId(round = round, id = id)
    }
