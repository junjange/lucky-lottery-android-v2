package com.junjange.presentation.ui.randomnumber

import com.junjange.presentation.base.BaseViewModel
import com.junjange.presentation.component.LottoType
import com.junjange.presentation.ui.randomnumber.RandomNumberContract.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@HiltViewModel
class RandomNumberViewModel
    @Inject
    constructor() : BaseViewModel() {
        private val _effect = Channel<Effect>(Channel.BUFFERED)
        val effect get() = _effect.receiveAsFlow()

        fun event(event: Event) {
            when (event) {
                is Event.Back -> finish()
                is Event.OnRandomNumberGenerationClick -> navigateToRandomNumberGeneration(lottoType = event.lottoType)
            }
        }

        private fun navigateToRandomNumberGeneration(lottoType: LottoType) {
            launch {
                _effect.send(Effect.NavigateToRandomNumberGeneration(lottoType = lottoType))
            }
        }

        private fun finish() {
            launch {
                _effect.send(Effect.Finish)
            }
        }
    }
