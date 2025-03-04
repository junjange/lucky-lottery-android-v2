package com.junjange.domain.usecase

import com.junjange.domain.model.PensionLotteryGetContent
import com.junjange.domain.repository.PensionLotteryRepository
import javax.inject.Inject

class LoadPensionLotteryRoundsUseCase
    @Inject
    constructor(
        private val repository: PensionLotteryRepository,
    ) {
        suspend operator fun invoke(
            page: Int,
            size: Int,
        ): Result<List<PensionLotteryGetContent>> = repository.loadPensionLotteryRounds(page = page, size = size)
    }
