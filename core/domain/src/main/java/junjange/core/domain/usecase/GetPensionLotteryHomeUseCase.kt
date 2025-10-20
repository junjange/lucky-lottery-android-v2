package junjange.core.domain.usecase

import junjange.core.domain.model.PensionLotteryHome
import junjange.core.domain.repository.WinningRepository
import javax.inject.Inject

class GetPensionLotteryHomeUseCase
    @Inject
    constructor(
        private val repository: WinningRepository,
    ) {
        suspend operator fun invoke(): Result<PensionLotteryHome> = repository.getPensionLotteryHome()
    }
