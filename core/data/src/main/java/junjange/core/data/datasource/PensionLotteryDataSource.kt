package junjange.core.data.datasource

import junjange.core.data.model.remote.PensionLotteryGetEntity
import junjange.core.data.model.remote.PensionLotteryRandomEntity

interface PensionLotteryDataSource {
    suspend fun postPensionLotterySave(
        pensionGroup: Int,
        pensionFirstNum: Int,
        pensionSecondNum: Int,
        pensionThirdNum: Int,
        pensionFourthNum: Int,
        pensionFifthNum: Int,
        pensionSixthNum: Int,
    ): Result<Unit>

    suspend fun getPensionLotteryRandom(): Result<PensionLotteryRandomEntity>

    suspend fun getPensionLotteryGet(
        page: Int,
        size: Int,
    ): Result<PensionLotteryGetEntity>
}
