package junjange.feature.home

import junjange.core.domain.model.LotteryNumbers
import junjange.core.domain.model.PensionLotteryHome

sealed interface HomeContract {
    data class State(
        val isLoading: Boolean = false,
        val isError: Boolean = false,
        val lotteryNumbers: LotteryNumbers? = null,
        val pensionLotteryHome: PensionLotteryHome? = null,
        val lotteryRound: Int = 0,
        val pensionLotteryRound: Int = 0,
    )

    sealed interface Event {
        data object Refresh : Event

        data class ChangeLottery(
            val offset: Int,
        ) : Event

        data class ChangePensionLottery(
            val offset: Int,
        ) : Event
    }

    sealed interface Effect {
        data class ShowMessage(
            val message: HomeMessage,
        ) : Effect
    }
}
