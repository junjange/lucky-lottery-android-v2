package com.junjange.domain.usecase

import com.junjange.domain.model.PensionLotteryHome
import com.junjange.domain.repository.PensionLotteryRepository
import javax.inject.Inject

class GetPensionLotteryUseCase
    @Inject
    constructor(
        private val repository: PensionLotteryRepository,
    ) {
        suspend operator fun invoke(drwNo: Int): Result<PensionLotteryHome> = repository.getPensionLottoNumber(drwNo = drwNo)
    }
