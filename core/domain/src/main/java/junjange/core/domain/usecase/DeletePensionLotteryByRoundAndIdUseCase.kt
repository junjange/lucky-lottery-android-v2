package junjange.core.domain.usecase

import junjange.core.domain.repository.PensionLotteryRepository
import javax.inject.Inject

class DeletePensionLotteryByRoundAndIdUseCase
    @Inject
    constructor(
        private val repository: PensionLotteryRepository,
    ) {
        suspend operator fun invoke(
            round: Int,
            id: Long,
        ): Result<Unit> = repository.deletePensionLotteryByRoundAndId(round = round, id = id)
    }
