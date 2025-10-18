package junjange.core.domain.usecase

import junjange.core.domain.model.LotteryNumbers
import junjange.core.domain.repository.LotteryRepository
import javax.inject.Inject

class GetLotteryUseCase
    @Inject
    constructor(
        private val repository: LotteryRepository,
    ) {
        suspend operator fun invoke(drwNo: Int): Result<LotteryNumbers> = repository.getLottoNumber(drwNo = drwNo)
    }
