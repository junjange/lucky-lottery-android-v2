package junjange.core.domain.usecase

import junjange.core.domain.model.PensionLotteryHome
import junjange.core.domain.repository.PensionLotteryRepository

class GetPensionLotteryUseCase
    
    constructor(
        private val repository: PensionLotteryRepository,
    ) {
        suspend operator fun invoke(drwNo: Int): Result<PensionLotteryHome> = repository.getPensionLottoNumber(drwNo = drwNo)
    }
