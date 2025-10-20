package junjange.core.data.mapper

import junjange.core.data.model.remote.UserMyInfoEntity
import junjange.core.domain.model.UserMyInfo

internal fun UserMyInfoEntity.toDomain() =
    UserMyInfo(
        id = id,
        nickname = nickname,
        oauthProvider = oauthProvider,
        email = email,
        profilePath = profilePath,
        lotteryNotificationStatus = lotteryNotificationStatus,
        pensionLotteryNotificationStatus = pensionLotteryNotificationStatus,
    )
