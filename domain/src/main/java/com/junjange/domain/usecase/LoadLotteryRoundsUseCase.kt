package com.junjange.domain.usecase

import com.junjange.domain.model.LotteryGetContent
import com.junjange.domain.repository.LotteryRepository
import javax.inject.Inject

class LoadLotteryRoundsUseCase
    @Inject
    constructor(
        private val repository: LotteryRepository,
    ) {
        suspend operator fun invoke(
            page: Int,
            size: Int,
        ): Result<List<LotteryGetContent>> = repository.loadLotteryRounds(page = page, size = size)
    }
