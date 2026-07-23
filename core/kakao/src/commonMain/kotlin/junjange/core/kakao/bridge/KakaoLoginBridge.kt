package junjange.core.kakao.bridge

/**
 * Platform login bridge for Kakao.
 *
 * Android performs the login directly with the Kakao SDK, so it does not need this.
 * iOS has no Kotlin/Native binding for the Kakao SDK, so the iOS app implements this
 * interface in Swift (using the Kakao iOS SDK) and registers it in Koin; the iOS
 * [junjange.core.data.datasource.KakaoLoginDataSource] then delegates to it.
 *
 * Callback-based with primitive parameters so it is trivial to implement from Swift.
 */
interface KakaoLoginBridge {
    fun login(
        onSuccess: (accessToken: String?, idToken: String?) -> Unit,
        onFailure: (message: String) -> Unit,
    )
}
