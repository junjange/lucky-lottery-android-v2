package junjange.feature.withdrawal

import junjange.core.domain.model.OauthProvider


data class WithdrawalState(
    val step: Int = 1,
    val isWithdrawalDialogShowing: Boolean = false,
    val oauthProvider: OauthProvider = OauthProvider.GOOGLE,
)
