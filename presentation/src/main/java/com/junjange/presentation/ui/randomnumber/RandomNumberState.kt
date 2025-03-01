package com.junjange.presentation.ui.randomnumber

sealed interface RandomNumberContract {
    sealed interface Event {
        data object Back : Event
    }

    sealed interface Effect {
        data object Finish : Effect
    }
}
