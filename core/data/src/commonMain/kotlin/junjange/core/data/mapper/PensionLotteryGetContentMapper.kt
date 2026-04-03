package junjange.core.data.mapper

import junjange.core.data.model.remote.PensionLotteryGetContentEntity
import junjange.core.domain.model.PensionLotteryGetContent

internal fun PensionLotteryGetContentEntity.toDomain() =
    PensionLotteryGetContent(
        round = round,
        winningDate = winningDate,
        checkWinningBonus = checkWinningBonus,
        pensionLotteryNumbers = pensionLotteryNumbersEntity.toDomain(),
        winningPensionLotteryNumbers = winningPensionLotteryNumbersEntity?.toDomain(),
        winningPensionLotteryBonusNumbers = winningPensionLotteryBonusNumbersEntity?.toDomain(),
    )
