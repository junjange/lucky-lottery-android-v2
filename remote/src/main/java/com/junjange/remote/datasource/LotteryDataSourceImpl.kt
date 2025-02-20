package com.junjange.remote.datasource

import com.junjange.data.datasource.LotteryDataSource
import com.junjange.data.model.remote.LotteryGetEntity
import com.junjange.data.model.remote.LotteryRandomNumbersEntity
import com.junjange.data.model.remote.LottoEntity
import com.junjange.data.model.remote.PensionLotteryHomeEntity
import com.junjange.remote.api.ApiService
import com.junjange.remote.api.LotteryService
import com.junjange.remote.model.request.LotteryRandomRequest
import com.junjange.remote.model.response.toData
import com.junjange.remote.model.response.toParseLotteryNumbers
import okhttp3.ResponseBody
import org.jsoup.Jsoup
import javax.inject.Inject

internal class LotteryDataSourceImpl
    @Inject
    constructor(
        private val apiService: ApiService,
        private val lotteryService: LotteryService,
    ) : LotteryDataSource {
        override suspend fun getLotteryRound(): Result<Int> = fetchLatestRound(selector = "#lottoDrwNo") { lotteryService.getRoundInfo() }

        override suspend fun getPensionLotteryRound(): Result<Int> =
            fetchLatestRound(selector = "#drwNo720") { lotteryService.getRoundInfo() }

        private suspend fun fetchLatestRound(
            selector: String,
            request: suspend () -> ResponseBody,
        ): Result<Int> =
            runCatching {
                request().use { body ->
                    val htmlContent = body.string()
                    val document = Jsoup.parse(htmlContent)
                    val roundText = document.select(selector).text()

                    roundText.toInt()
                }
            }

        override suspend fun getLotteryGet(
            page: Int,
            size: Int,
        ): Result<LotteryGetEntity> =
            runCatching {
                apiService.getLotteryGet(page = page, size = size).data.toData()
            }

        override suspend fun postLotterySave(
            firstNum: Int,
            secondNum: Int,
            thirdNum: Int,
            fourthNum: Int,
            fifthNum: Int,
            sixthNum: Int,
        ): Result<Unit> =
            runCatching {
                val body =
                    LotteryRandomRequest(
                        firstNum = firstNum,
                        secondNum = secondNum,
                        thirdNum = thirdNum,
                        fourthNum = fourthNum,
                        fifthNum = fifthNum,
                        sixthNum = sixthNum,
                    )
                apiService.postLotterySave(body = body)
            }

        override suspend fun getLotteryRandom(): Result<LotteryRandomNumbersEntity> =
            runCatching {
                apiService.getLotteryRandom().data.toData()
            }

        override suspend fun getLottoNumber(drwNo: Int): Result<LottoEntity> =
            runCatching {
                lotteryService.getLottoNumber(drwNo = drwNo).toData()
            }

        override suspend fun getPensionLottoNumber(drwNo: Int): Result<PensionLotteryHomeEntity> =
            runCatching {
                val responseBody = lotteryService.getPensionLottoNumber(round = drwNo)
                responseBody.toParseLotteryNumbers()
            }
    }
