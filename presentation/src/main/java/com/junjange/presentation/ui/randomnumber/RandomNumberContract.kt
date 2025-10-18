package com.junjange.presentation.ui.randomnumber

import junjange.core.ui.component.LottoType


sealed interface RandomNumberContract {
    sealed interface Event {
        data object Back : Event

        data class OnRandomNumberGenerationClick(
            val lottoType: LottoType,
        ) : Event
    }

    sealed interface Effect {
        data object Finish : Effect

        data class NavigateToRandomNumberGeneration(
            val lottoType: LottoType,
        ) : Effect
    }
}
