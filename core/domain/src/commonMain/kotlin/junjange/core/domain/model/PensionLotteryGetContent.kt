package junjange.core.domain.model

data class PensionLotteryGetContent(
    val round: Int,
    val winningDate: String,
    val checkWinningBonus: Boolean,
    val pensionLotteryNumbers: List<PensionLotteryNumbers>,
    val winningPensionLotteryNumbers: WinningPensionLotteryNumbers?,
    val winningPensionLotteryBonusNumbers: WinningPensionLotteryBonusNumbers?,
)
