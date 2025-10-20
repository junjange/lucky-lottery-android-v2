package junjange.core.domain.model

data class UserMyInfo(
    val id: Int,
    val nickname: String,
    val oauthProvider: String,
    val email: String?,
    val profilePath: String?,
    val lotteryNotificationStatus: Boolean,
    val pensionLotteryNotificationStatus: Boolean,
)
