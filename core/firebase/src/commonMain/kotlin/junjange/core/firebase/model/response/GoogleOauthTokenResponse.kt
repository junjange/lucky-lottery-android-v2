package junjange.core.firebase.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import junjange.core.data.model.remote.GoogleOauthTokenEntity

@Serializable
data class GoogleOauthTokenResponse(
    @SerialName("access_token")
    val accessToken: String = "",
    @SerialName("expires_in")
    val expiresIn: Int = 0,
    @SerialName("scope")
    val scope: String = "",
    @SerialName("token_type")
    val tokenType: String = "",
    @SerialName("id_token")
    val idToken: String = "",
)

internal fun GoogleOauthTokenResponse.toData() =
    GoogleOauthTokenEntity(
        accessToken = accessToken,
        expiresIn = expiresIn,
        scope = scope,
        tokenType = tokenType,
        idToken = idToken,
    )
