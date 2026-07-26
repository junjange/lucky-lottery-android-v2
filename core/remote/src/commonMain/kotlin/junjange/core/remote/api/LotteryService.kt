package junjange.core.remote.api

import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Query
import junjange.core.remote.model.response.Lotto645InfoResponse
import junjange.core.remote.model.response.PensionLottery720PrizeResponse

interface LotteryService {
    /**
     * 로또 645 당첨 정보 조회
     * @param round 회차 번호 (null이면 최신 회차)
     */
    @GET("lt645/selectPstLt645Info.do")
    suspend fun getLottoInfo(
        @Query("srchLtEpsd") round: Int? = null,
    ): Lotto645InfoResponse

    /**
     * 연금복권 720+ 당첨 정보 조회 (당첨 번호 + 당첨금 정보)
     * @param round 회차 번호 (null이면 최신 회차)
     */
    @GET("pt720/selectPstPt720Info.do")
    suspend fun getPensionLotteryInfo(
        @Query("srchPsltEpsd") round: Int? = null,
    ): PensionLottery720PrizeResponse
}
