package junjange.core.firebase.model.response

import com.google.gson.annotations.SerializedName
import junjange.core.data.model.remote.GoogleOauthTokenEntity

data class GoogleOauthTokenResponse(
    @SerializedName("access_token")
    var accessToken: String = "",
    @SerializedName("expires_in")
    var expiresIn: Int = 0,
    @SerializedName("scope")
    var scope: String = "",
    @SerializedName("token_type")
    var tokenType: String = "",
    @SerializedName("id_token")
    var idToken: String = "",
)

internal fun GoogleOauthTokenResponse.toData() =
    GoogleOauthTokenEntity(
        accessToken = accessToken,
        expiresIn = expiresIn,
        scope = scope,
        tokenType = tokenType,
        idToken = idToken,
    )
