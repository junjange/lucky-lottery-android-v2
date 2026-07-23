package junjange.feature.randomnumber

import junjange.core.domain.model.LottoType

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
