package junjange.core.data.model.remote

data class GoogleOauthTokenEntity(
    var accessToken: String = "",
    var expiresIn: Int = 0,
    var scope: String = "",
    var tokenType: String = "",
    var idToken: String = "",
)
