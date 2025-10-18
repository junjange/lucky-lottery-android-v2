package junjange.core.domain.usecase

import junjange.core.domain.repository.PensionLotteryRepository
import javax.inject.Inject

class InsertPensionLotteryUseCase
    @Inject
    constructor(
        private val repository: PensionLotteryRepository,
    ) {
        suspend operator fun invoke(
            group: Int,
            firstNum: Int,
            secondNum: Int,
            thirdNum: Int,
            fourthNum: Int,
            fifthNum: Int,
            sixthNum: Int,
        ): Result<Unit> =
            repository.insertPensionLottery(
                group = group,
                firstNum = firstNum,
                secondNum = secondNum,
                thirdNum = thirdNum,
                fourthNum = fourthNum,
                fifthNum = fifthNum,
                sixthNum = sixthNum,
            )
    }
