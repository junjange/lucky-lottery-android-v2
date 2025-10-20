package junjange.feature.withdrawal

sealed class WithdrawalEffect {
    data object AddStep : WithdrawalEffect()
}
