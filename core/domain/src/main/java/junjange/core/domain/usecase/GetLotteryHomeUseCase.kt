package junjange.core.domain.usecase

import junjange.core.domain.model.LotteryNumbers
import junjange.core.domain.repository.WinningRepository
import javax.inject.Inject

class GetLotteryHomeUseCase
    @Inject
    constructor(
        private val repository: WinningRepository,
    ) {
        suspend operator fun invoke(): Result<LotteryNumbers> = repository.getLotteryHome()
    }
