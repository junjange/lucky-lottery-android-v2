package com.junjange.presentation.ui.home

import com.junjange.domain.model.LotteryNumbers
import com.junjange.domain.model.PensionLotteryHome

sealed interface HomeContract {
    data class State(
        val isLoading: Boolean = false,
        val lotteryNumbers: LotteryNumbers? = null,
        val pensionLotteryHome: PensionLotteryHome? = null,
        val lotteryRound: Int = 0,
        val pensionLotteryRound: Int = 0,
    )

    sealed interface Event {
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

fun Long.formatPrizeAmount(): String =
    when {
        this >= 100_000_000 -> "${this / 100_000_000}억"
        this >= 10_000 -> "${this / 10_000}만원"
        else -> "${this}원"
    }
