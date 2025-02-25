package com.junjange.domain.usecase

import com.junjange.domain.repository.PensionLotteryRepository
import javax.inject.Inject

class GetPensionLotteryRoundUseCase
    @Inject
    constructor(
        private val repository: PensionLotteryRepository,
    ) {
        suspend operator fun invoke(): Result<Int> = repository.getPensionLotteryRound()
    }
