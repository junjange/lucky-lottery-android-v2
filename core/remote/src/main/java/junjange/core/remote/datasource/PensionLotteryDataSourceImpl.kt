package junjange.core.remote.datasource

import junjange.core.data.datasource.PensionLotteryDataSource
import junjange.core.data.model.remote.PensionLotteryGetEntity
import junjange.core.data.model.remote.PensionLotteryRandomEntity
import junjange.core.remote.api.ApiService
import junjange.core.remote.model.request.PensionLotteryRandomRequest
import junjange.core.remote.model.response.toData
import javax.inject.Inject

internal class PensionLotteryDataSourceImpl
    @Inject
    constructor(
        private val apiService: ApiService,
    ) : PensionLotteryDataSource {
        override suspend fun postPensionLotterySave(
            pensionGroup: Int,
            pensionFirstNum: Int,
            pensionSecondNum: Int,
            pensionThirdNum: Int,
            pensionFourthNum: Int,
            pensionFifthNum: Int,
            pensionSixthNum: Int,
        ): Result<Unit> =
            runCatching {
                val body =
                    PensionLotteryRandomRequest(
                        pensionGroup = pensionGroup,
                        pensionFirstNum = pensionFirstNum,
                        pensionSecondNum = pensionSecondNum,
                        pensionThirdNum = pensionThirdNum,
                        pensionFourthNum = pensionFourthNum,
                        pensionFifthNum = pensionFifthNum,
                        pensionSixthNum = pensionSixthNum,
                    )
                apiService.postPensionLotterySave(body = body)
            }

        override suspend fun getPensionLotteryRandom(): Result<PensionLotteryRandomEntity> =
            runCatching {
                apiService.getPensionLotteryRandom().data.toData()
            }

        override suspend fun getPensionLotteryGet(
            page: Int,
            size: Int,
        ): Result<PensionLotteryGetEntity> =
            runCatching {
                apiService.getPensionLotteryGet(
                    page = page,
                    size = size,
                ).data.toData()
            }
    }
