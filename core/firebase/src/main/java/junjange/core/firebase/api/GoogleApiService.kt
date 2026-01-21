package junjange.core.firebase.api

import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.POST
import junjange.core.firebase.model.request.GoogleOauthTokenRequest
import junjange.core.firebase.model.response.GoogleOauthTokenResponse

internal interface GoogleApiService {
    @POST(GoogleApiClient.OAUTH2.POST_OAUTH2_TOKEN)
    suspend fun postOauthToken(
        @Body body: GoogleOauthTokenRequest,
    ): GoogleOauthTokenResponse
}
