package junjange.core.firebase.model.request

import com.google.gson.annotations.SerializedName

data class GoogleOauthTokenRequest(
    @SerializedName("grant_type")
    private val grantType: String,
    @SerializedName("client_id")
    private val clientId: String,
    @SerializedName("client_secret")
    private val clientSecret: String,
    @SerializedName("redirect_uri")
    private val redirectUri: String,
    @SerializedName("code")
    private val code: String,
)
