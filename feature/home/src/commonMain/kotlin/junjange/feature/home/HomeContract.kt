package junjange.feature.home

import junjange.core.domain.model.LotteryNumbers
import junjange.core.domain.model.PensionLotteryHome

/** 두 복권 모두 1회차부터 시작한다. */
private const val FIRST_ROUND = 1

sealed interface HomeContract {
    data class State(
        val isLoading: Boolean = false,
        val isError: Boolean = false,
        val lotteryNumbers: LotteryNumbers? = null,
        val pensionLotteryHome: PensionLotteryHome? = null,
        val lotteryRound: Int = 0,
        val pensionLotteryRound: Int = 0,
        // 서버가 알려 준 최신 회차. 다음 회차 버튼을 잠글 상한이다.
        val latestLotteryRound: Int = 0,
        val latestPensionLotteryRound: Int = 0,
    ) {
        // 1회차가 하한, 최신 회차가 상한이다. 회차를 아직 못 받았으면(0) 양쪽 다 잠근다.
        val canGoPreviousLottery: Boolean get() = lotteryRound > FIRST_ROUND
        val canGoNextLottery: Boolean get() = lotteryRound in FIRST_ROUND..<latestLotteryRound
        val canGoPreviousPension: Boolean get() = pensionLotteryRound > FIRST_ROUND
        val canGoNextPension: Boolean
            get() = pensionLotteryRound in FIRST_ROUND..<latestPensionLotteryRound
    }

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
