package junjange.core.domain.usecase

import junjange.core.domain.model.GoogleOauthToken
import junjange.core.domain.repository.GoogleRepository
import javax.inject.Inject

class PostGoogleOauthTokenUseCase
    @Inject
    constructor(
        private val repository: GoogleRepository,
    ) {
        suspend operator fun invoke(
            grantType: String,
            clientId: String,
            clientSecret: String,
            redirectUri: String,
            code: String,
        ): Result<GoogleOauthToken> =
            repository.postOauthToken(
                grantType = grantType,
                clientId = clientId,
                clientSecret = clientSecret,
                redirectUri = redirectUri,
                code = code,
            )
    }
