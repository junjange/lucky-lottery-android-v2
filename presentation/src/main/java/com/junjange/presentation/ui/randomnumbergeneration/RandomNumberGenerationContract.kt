package com.junjange.presentation.ui.randomnumbergeneration

import junjange.core.domain.model.LotteryRandomNumbers
import junjange.core.domain.model.PensionLotteryRandom
import com.junjange.presentation.ui.randomnumber.RandomNumberMessage

sealed interface RandomNumberGenerationContract {
    data class State(
        val isLoading: Boolean = false,
        val isLotto645: Boolean = true,
        val saveIsEnabled: Boolean = false,
        val lotteryRandomNumbers: LotteryRandomNumbers? = null,
        val pensionLotteryRandom: PensionLotteryRandom? = null,
    )

    sealed interface Event {
        data object Back : Event

        data object GenerateRandomLottery : Event

        data object GenerateRandomPensionLottery : Event

        data object SaveLottery : Event

        data object SavePensionLottery : Event
    }

    sealed interface Effect {
        data object Finish : Effect

        data class ShowMessage(
            val message: RandomNumberMessage,
        ) : Effect
    }
}
