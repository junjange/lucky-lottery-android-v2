package junjange.core.remote.model.response

import junjange.core.data.model.remote.UserMyInfoEntity

data class UserMyInfoResponse(
    val id: Int,
    val nickname: String,
    val oauthProvider: String,
    val email: String?,
    val profilePath: String?,
    val lotteryNotificationStatus: Boolean,
    val pensionLotteryNotificationStatus: Boolean,
)

internal fun UserMyInfoResponse.toData() =
    UserMyInfoEntity(
        id = id,
        nickname = nickname,
        oauthProvider = oauthProvider,
        email = email,
        profilePath = profilePath,
        lotteryNotificationStatus = lotteryNotificationStatus,
        pensionLotteryNotificationStatus = pensionLotteryNotificationStatus,
    )
