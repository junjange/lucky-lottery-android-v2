package com.junjange.domain.usecase

import com.junjange.domain.model.LotteryNumbers
import com.junjange.domain.repository.LotteryRepository
import javax.inject.Inject

class GetLotteryUseCase
    @Inject
    constructor(
        private val repository: LotteryRepository,
    ) {
        suspend operator fun invoke(drwNo: Int): Result<LotteryNumbers> = repository.getLottoNumber(drwNo = drwNo)
    }
