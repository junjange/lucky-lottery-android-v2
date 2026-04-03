package junjange.core.remote.datasource

import junjange.core.data.datasource.WinningDataSource
import junjange.core.data.model.remote.LotteryNumbersEntity
import junjange.core.data.model.remote.PensionLotteryHomeEntity
import junjange.core.remote.api.ApiService
import junjange.core.remote.model.response.toData

internal class WinningDataSourceImpl
    
    constructor(
        private val apiService: ApiService,
    ) : WinningDataSource {
        override suspend fun getWinningLotteryHome(): Result<LotteryNumbersEntity> =
            runCatching {
                apiService.getLotteryHome().data.toData()
            }

        override suspend fun getWinningPensionLotteryHome(): Result<PensionLotteryHomeEntity> =
            runCatching {
                apiService.getPensionLotteryHome().data.toData()
            }
    }
