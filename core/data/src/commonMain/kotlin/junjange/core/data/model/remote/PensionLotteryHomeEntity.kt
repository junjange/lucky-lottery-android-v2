package junjange.core.data.model.remote

data class PensionLotteryHomeEntity(
    val round: Int,
    val winningDate: String,
    val lotteryGroup: Int,
    val winningFirstNum: Int,
    val winningSecondNum: Int,
    val winningThirdNum: Int,
    val winningFourthNum: Int,
    val winningFifthNum: Int,
    val winningSixthNum: Int,
    val bonusFirstNum: Int,
    val bonusSecondNum: Int,
    val bonusThirdNum: Int,
    val bonusFourthNum: Int,
    val bonusFifthNum: Int,
    val bonusSixthNum: Int,
)
