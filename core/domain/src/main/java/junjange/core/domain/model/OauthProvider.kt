package junjange.core.domain.model


enum class OauthProvider(val displayName: String) {
    KAKAO("Kakao 로그인"),
    GOOGLE("Google 로그인"),
    ;

    companion object {
        fun from(value: String): OauthProvider {
            return OauthProvider.entries.find { it.name == value } ?: KAKAO
        }
    }
}
