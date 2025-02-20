package com.junjange.domain.usecase

import com.junjange.domain.repository.LotteryRepository
import javax.inject.Inject

class GetPensionLotteryRoundUseCase
    @Inject
    constructor(
        private val repository: LotteryRepository,
    ) {
        suspend operator fun invoke(): Result<Int> = repository.getPensionLotteryRound()
    }
