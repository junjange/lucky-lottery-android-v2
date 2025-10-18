package junjange.core.domain.usecase

import junjange.core.domain.repository.LotteryRepository
import javax.inject.Inject

class InsertLotteryUseCase
    @Inject
    constructor(
        private val repository: LotteryRepository,
    ) {
        suspend operator fun invoke(
            firstNum: Int,
            secondNum: Int,
            thirdNum: Int,
            fourthNum: Int,
            fifthNum: Int,
            sixthNum: Int,
        ): Result<Unit> =
            repository.insertLottery(
                firstNum = firstNum,
                secondNum = secondNum,
                thirdNum = thirdNum,
                fourthNum = fourthNum,
                fifthNum = fifthNum,
                sixthNum = sixthNum,
            )
    }
