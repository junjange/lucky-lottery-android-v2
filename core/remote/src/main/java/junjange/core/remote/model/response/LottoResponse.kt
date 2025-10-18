package junjange.core.remote.model.response

import junjange.core.data.model.remote.LottoEntity

data class LottoResponse(
    val totSellamnt: Long,
    val returnValue: String,
    val drwNoDate: String,
    val firstWinamnt: Long,
    val drwtNo6: Int,
    val drwtNo4: Int,
    val firstPrzwnerCo: Int,
    val drwtNo5: Int,
    val bnusNo: Int,
    val firstAccumamnt: Long,
    val drwNo: Int,
    val drwtNo2: Int,
    val drwtNo3: Int,
    val drwtNo1: Int,
)

internal fun LottoResponse.toData(): LottoEntity =
    LottoEntity(
        totSellamnt = totSellamnt,
        returnValue = returnValue,
        drwNoDate = drwNoDate,
        firstWinamnt = firstWinamnt,
        drwtNo6 = drwtNo6,
        drwtNo4 = drwtNo4,
        firstPrzwnerCo = firstPrzwnerCo,
        drwtNo5 = drwtNo5,
        bnusNo = bnusNo,
        firstAccumamnt = firstAccumamnt,
        drwNo = drwNo,
        drwtNo2 = drwtNo2,
        drwtNo3 = drwtNo3,
        drwtNo1 = drwtNo1,
    )
