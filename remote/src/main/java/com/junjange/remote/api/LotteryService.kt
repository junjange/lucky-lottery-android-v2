package com.junjange.remote.api

import com.junjange.remote.model.response.LottoResponse
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

    @GET("gameResult.do")
    suspend fun getPensionLottoNumber(
        @Query("method") method: String = "win720",
        @Query("Round") round: Int,
    ): ResponseBody
}
