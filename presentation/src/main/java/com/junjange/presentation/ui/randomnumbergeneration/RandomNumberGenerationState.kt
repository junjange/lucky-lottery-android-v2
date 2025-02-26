package com.junjange.presentation.ui.randomnumbergeneration

import com.junjange.domain.model.LotteryRandomNumbers
import com.junjange.domain.model.PensionLotteryRandom

data class RandomNumberGenerationState(
    val isLoading: Boolean = false,
    val isLotto645: Boolean = true,
    val saveIsEnabled: Boolean = false,
    val lotteryRandomNumbers: LotteryRandomNumbers? = null,
    val pensionLotteryRandom: PensionLotteryRandom? = null,
)
