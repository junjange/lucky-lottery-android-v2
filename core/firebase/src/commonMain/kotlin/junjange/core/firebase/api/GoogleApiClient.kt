package junjange.core.firebase.api

class GoogleApiClient {
    object OAUTH2 {
        private const val BASE = "/oauth2/v4/"
        const val POST_OAUTH2_TOKEN = BASE + "token"
    }
}
