package junjange.core.domain.usecase

import junjange.core.domain.model.LotteryNumbers
import junjange.core.domain.repository.LotteryRepository

class GetLotteryUseCase
    
    constructor(
        private val repository: LotteryRepository,
    ) {
        suspend operator fun invoke(drwNo: Int): Result<LotteryNumbers> = repository.getLottoNumber(drwNo = drwNo)
    }
