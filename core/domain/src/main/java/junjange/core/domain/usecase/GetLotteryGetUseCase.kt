package junjange.core.domain.usecase

import junjange.core.domain.model.LotteryGet
import junjange.core.domain.repository.LotteryRepository
import javax.inject.Inject

class GetLotteryGetUseCase
    @Inject
    constructor(
        private val repository: LotteryRepository,
    ) {
        suspend operator fun invoke(
            page: Int,
            size: Int,
        ): Result<LotteryGet> =
            repository.getLotteryGet(
                page = page,
                size = size,
            )
    }
