package junjange.core.data.model.remote

data class PensionLotteryGetContentEntity(
    val round: Int,
    val winningDate: String,
    val checkWinningBonus: Boolean,
    val pensionLotteryNumbersEntity: List<PensionLotteryNumbersEntity>,
    val winningPensionLotteryNumbersEntity: WinningPensionLotteryNumbersEntity?,
    val winningPensionLotteryBonusNumbersEntity: WinningPensionLotteryBonusNumbersEntity?,
)
