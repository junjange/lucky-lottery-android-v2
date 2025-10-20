package junjange.core.data.mapper

import junjange.core.data.model.remote.LotteryGetContentEntity
import junjange.core.domain.model.LotteryGetContent

internal fun LotteryGetContentEntity.toDomain() =
    LotteryGetContent(
        round = round,
        winningDate = winningDate,
        lotteryGetNumbers = lotteryGetNumbersEntity.toDomain(),
        winningLotteryNumbers = winningLotteryNumbersEntity?.toDomain(),
    )
