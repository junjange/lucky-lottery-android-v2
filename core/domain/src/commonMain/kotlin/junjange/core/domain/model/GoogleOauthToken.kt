package junjange.core.domain.model

data class GoogleOauthToken(
    var accessToken: String = "",
    var expiresIn: Int = 0,
    var scope: String = "",
    var tokenType: String = "",
    var idToken: String = "",
)
