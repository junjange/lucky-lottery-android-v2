package junjange.core.remote.api

import junjange.core.remote.model.response.LottoResponse
import junjange.core.remote.model.response.PensionLottery720PrizeResponse
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query

interface LotteryService {
    @GET("common.do")
    suspend fun getRoundInfo(
        @Query("method") method: String = "main",
    ): ResponseBody

    @GET("common.do")
    suspend fun getLottoNumber(
        @Query("method") method: String = "getLottoNumber",
        @Query("drwNo") drwNo: Int,
    ): LottoResponse

    /**
     * 연금복권 720+ 당첨 정보 조회 (당첨 번호 + 당첨금 정보)
     * @param round 회차 번호 (null이면 최신 회차)
     */
    @GET("pt720/selectPstPt720Info.do")
    suspend fun getPensionLotteryInfo(
        @Query("srchPsltEpsd") round: Int? = null,
    ): PensionLottery720PrizeResponse
}
