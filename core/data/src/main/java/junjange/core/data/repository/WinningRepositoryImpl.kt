package junjange.core.data.repository

import junjange.core.data.datasource.WinningDataSource
import junjange.core.data.mapper.toDomain
import junjange.core.domain.model.LotteryNumbers
import junjange.core.domain.model.PensionLotteryHome
import junjange.core.domain.repository.WinningRepository
import javax.inject.Inject

internal class WinningRepositoryImpl
    @Inject
    constructor(
        private val dataSource: WinningDataSource,
    ) : WinningRepository {
        override suspend fun getLotteryHome(): Result<LotteryNumbers> = dataSource.getWinningLotteryHome().mapCatching { it.toDomain() }

        override suspend fun getPensionLotteryHome(): Result<PensionLotteryHome> =
            dataSource.getWinningPensionLotteryHome().mapCatching { it.toDomain() }
    }
