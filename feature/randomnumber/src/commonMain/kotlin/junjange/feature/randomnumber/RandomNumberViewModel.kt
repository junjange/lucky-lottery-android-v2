package junjange.feature.randomnumber

import junjange.core.domain.model.LottoType
import junjange.core.ui.base.BaseViewModel
import junjange.feature.randomnumber.RandomNumberContract.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow


class RandomNumberViewModel
    
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
