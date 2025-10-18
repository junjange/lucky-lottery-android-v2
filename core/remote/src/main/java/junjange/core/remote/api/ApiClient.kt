package junjange.core.remote.api

class ApiClient {
    object Credentials {
        private const val BASE = "/api/v1/credentials/"
        const val POST_CREDENTIALS_REGISTER = BASE + "register"
        const val POST_CREDENTIALS_REFRESH = BASE + "refresh"
        const val POST_CREDENTIALS_LOGIN = BASE + "login"
        const val GET_CREDENTIALS_VALID_REGISTER = BASE + "oauth/valid/register"
        const val POST_CREDENTIALS_LOGOUT = BASE + "logout"
        const val DELETE_CREDENTIALS_DELETE_ME = BASE + "delete/me"
    }

    object User {
        private const val BASE = "/api/v1/user/"
        const val PATCH_LOTTERY_NOTIFICATION = BASE + "lottery/notification"
        const val PATCH_PENSION_LOTTERY_NOTIFICATION = BASE + "pension/lottery/notification"
        const val GET_USER_MY_INFO = BASE + "my/info"
        const val PATCH_USER_MY_INFO = BASE + "update/my/info"
    }

    object Lottery {
        private const val BASE = "/api/v1/lottery/"
        const val POST_LOTTERY_SAVE = BASE + "save"
        const val GET_LOTTERY_RANDOM = BASE + "random"
        const val GET_LOTTERY_GET = BASE + "get"
    }

    object PensionLottery {
        private const val BASE = "/api/v1/pension/lottery/"
        const val POST_PENSION_LOTTERY_SAVE = BASE + "save"
        const val GET_PENSION_LOTTERY_RANDOM = BASE + "random"
        const val GET_PENSION_LOTTERY_GET = BASE + "get"
    }

    object Winning {
        private const val BASE = "/api/v1/winning/"
        const val GET_WINNING_LOTTERY_HOME = BASE + "lottery/recent/round"
        const val GET_WINNING_PENSION_LOTTERY_HOME = BASE + "pension/lottery/home"
    }

    object Notification {
        private const val BASE = "/api/v1/notification/"
        const val POST_NOTIFICATION_REGISTER_TOKEN = BASE + "register/token"
    }

    object Images {
        private const val BASE = "/api/v1/images/"
        const val POST_IMAGES_UPLOAD = BASE + "upload"
    }
}
