package junjange.core.data.model.remote

data class LotteryGetContentEntity(
    val round: Int,
    val winningDate: String,
    val lotteryGetNumbersEntity: List<LotteryGetNumbersEntity>,
    val winningLotteryNumbersEntity: WinningLotteryNumbersEntity?,
)
