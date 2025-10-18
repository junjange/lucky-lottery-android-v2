package junjange.core.domain.model

data class LotteryGetContent(
    val round: Int,
    val winningDate: String,
    val lotteryGetNumbers: List<LotteryGetNumbers>,
    val winningLotteryNumbers: WinningLotteryNumbers?,
)
