package junjange.core.remote.api

import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.DELETE
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Multipart
import de.jensklingenberg.ktorfit.http.PATCH
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.Part
import de.jensklingenberg.ktorfit.http.Query
import io.ktor.client.request.forms.MultiPartFormDataContent
import junjange.core.remote.model.BaseResponse
import junjange.core.remote.model.request.LotteryRandomRequest
import junjange.core.remote.model.request.NotificationRegisterRequest
import junjange.core.remote.model.request.NotificationRequest
import junjange.core.remote.model.request.PensionLotteryRandomRequest
import junjange.core.remote.model.request.RefreshRequest
import junjange.core.remote.model.request.RegisterRequest
import junjange.core.remote.model.request.UserMyInfoRequest
import junjange.core.remote.model.response.ImagesUploadResponse
import junjange.core.remote.model.response.IsRegisteredResponse
import junjange.core.remote.model.response.JwtTokenResponse
import junjange.core.remote.model.response.LotteryGetResponse
import junjange.core.remote.model.response.LotteryNumbersResponse
import junjange.core.remote.model.response.LotteryRandomNumbersResponse
import junjange.core.remote.model.response.PensionLotteryGetResponse
import junjange.core.remote.model.response.PensionLotteryHomeResponse
import junjange.core.remote.model.response.PensionLotteryRandomResponse
import junjange.core.remote.model.response.UserMyInfoResponse

internal interface ApiService {
    @POST(ApiClient.Credentials.POST_CREDENTIALS_REGISTER)
    suspend fun postRegister(
        @Query("idToken") idToken: String,
        @Query("provider") provider: String,
        @Body body: RegisterRequest,
    ): BaseResponse<JwtTokenResponse>

    @POST(ApiClient.Credentials.POST_CREDENTIALS_REFRESH)
    suspend fun postRefresh(
        @Body body: RefreshRequest,
    ): BaseResponse<JwtTokenResponse>

    @POST(ApiClient.Credentials.POST_CREDENTIALS_LOGIN)
    suspend fun postLogin(
        @Query("idToken") idToken: String,
        @Query("provider") provider: String,
    ): BaseResponse<JwtTokenResponse>

    @POST(ApiClient.Credentials.POST_CREDENTIALS_LOGOUT)
    suspend fun postLogout(): BaseResponse<Unit>

    @DELETE(ApiClient.Credentials.DELETE_CREDENTIALS_DELETE_ME)
    suspend fun deleteMe(
        @Query("oauth_access_token") oauthAccessToken: String?,
    ): BaseResponse<Unit>

    @GET(ApiClient.Credentials.GET_CREDENTIALS_VALID_REGISTER)
    suspend fun getValidRegister(
        @Query("idToken") idToken: String,
        @Query("provider") provider: String,
    ): BaseResponse<IsRegisteredResponse>

    @PATCH(ApiClient.User.PATCH_LOTTERY_NOTIFICATION)
    suspend fun patchLotteryNotification(
        @Body body: NotificationRequest,
    ): BaseResponse<Unit>

    @PATCH(ApiClient.User.PATCH_PENSION_LOTTERY_NOTIFICATION)
    suspend fun patchPensionLotteryNotification(
        @Body body: NotificationRequest,
    ): BaseResponse<Unit>

    @GET(ApiClient.User.GET_USER_MY_INFO)
    suspend fun getUserMyIn(): BaseResponse<UserMyInfoResponse>

    @PATCH(ApiClient.User.PATCH_USER_MY_INFO)
    suspend fun patchUserMyInfo(
        @Body body: UserMyInfoRequest,
    ): BaseResponse<Unit>

    @POST(ApiClient.Lottery.POST_LOTTERY_SAVE)
    suspend fun postLotterySave(
        @Body body: LotteryRandomRequest,
    ): BaseResponse<Unit>

    @GET(ApiClient.Lottery.GET_LOTTERY_RANDOM)
    suspend fun getLotteryRandom(): BaseResponse<LotteryRandomNumbersResponse>

    @GET(ApiClient.Lottery.GET_LOTTERY_GET)
    suspend fun getLotteryGet(
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): BaseResponse<LotteryGetResponse>

    @POST(ApiClient.PensionLottery.POST_PENSION_LOTTERY_SAVE)
    suspend fun postPensionLotterySave(
        @Body body: PensionLotteryRandomRequest,
    ): BaseResponse<Unit>

    @GET(ApiClient.PensionLottery.GET_PENSION_LOTTERY_RANDOM)
    suspend fun getPensionLotteryRandom(): BaseResponse<PensionLotteryRandomResponse>

    @GET(ApiClient.PensionLottery.GET_PENSION_LOTTERY_GET)
    suspend fun getPensionLotteryGet(
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): BaseResponse<PensionLotteryGetResponse>

    @GET(ApiClient.Winning.GET_WINNING_LOTTERY_HOME)
    suspend fun getLotteryHome(): BaseResponse<LotteryNumbersResponse>

    @GET(ApiClient.Winning.GET_WINNING_PENSION_LOTTERY_HOME)
    suspend fun getPensionLotteryHome(): BaseResponse<PensionLotteryHomeResponse>

    @GET(ApiClient.Notification.POST_NOTIFICATION_REGISTER_TOKEN)
    suspend fun postNotificationRegisterToken(
        @Body body: NotificationRegisterRequest,
    ): BaseResponse<Unit>

    @Multipart
    @POST(ApiClient.Images.POST_IMAGES_UPLOAD)
    suspend fun postImagesUpload(
        @Part file: MultiPartFormDataContent,
    ): BaseResponse<ImagesUploadResponse>
}
