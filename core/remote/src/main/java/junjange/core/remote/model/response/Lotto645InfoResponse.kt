package junjange.core.remote.model.response

import com.google.gson.annotations.SerializedName
import junjange.core.data.model.remote.LottoEntity
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * 로또 645 당첨 정보 API 응답 모델
 * API: /lt645/selectPstLt645Info.do?srchLtEpsd={회차}
 */
data class Lotto645InfoResponse(
    @SerializedName("resultCode")
    val resultCode: String?,
    @SerializedName("resultMessage")
    val resultMessage: String?,
    @SerializedName("data")
    val data: Lotto645Data?,
)

data class Lotto645Data(
    @SerializedName("list")
    val list: List<Lotto645Item>?,
)

data class Lotto645Item(
    @SerializedName("ltEpsd")
    val round: Int,
    @SerializedName("tm1WnNo")
    val number1: Int,
    @SerializedName("tm2WnNo")
    val number2: Int,
    @SerializedName("tm3WnNo")
    val number3: Int,
    @SerializedName("tm4WnNo")
    val number4: Int,
    @SerializedName("tm5WnNo")
    val number5: Int,
    @SerializedName("tm6WnNo")
    val number6: Int,
    @SerializedName("bnsWnNo")
    val bonusNumber: Int,
    @SerializedName("ltRflYmd")
    val drawDate: String,
    @SerializedName("rnk1WnNope")
    val rank1WinnerCount: Int,
    @SerializedName("rnk1WnAmt")
    val rank1PrizeAmount: Long,
    @SerializedName("rnk1SumWnAmt")
    val rank1TotalAmount: Long,
    @SerializedName("wholEpsdSumNtslAmt")
    val totalSalesAmount: Long,
)

/**
 * 로또 645 응답에서 LottoEntity로 변환
 */
internal fun Lotto645InfoResponse.toData(requestedRound: Int): LottoEntity? {
    val item = data?.list?.firstOrNull { it.round == requestedRound } ?: return null

    // ltRflYmd: 추첨일 (예: "20260110") -> "2026-01-10"
    val formattedDate = formatLottoDate(item.drawDate)

    return LottoEntity(
        totSellamnt = item.totalSalesAmount,
        returnValue = "success",
        drwNoDate = formattedDate,
        firstWinamnt = item.rank1PrizeAmount,
        drwtNo6 = item.number6,
        drwtNo4 = item.number4,
        firstPrzwnerCo = item.rank1WinnerCount,
        drwtNo5 = item.number5,
        bnusNo = item.bonusNumber,
        firstAccumamnt = item.rank1TotalAmount,
        drwNo = item.round,
        drwtNo2 = item.number2,
        drwtNo3 = item.number3,
        drwtNo1 = item.number1,
    )
}

private fun formatLottoDate(dateString: String): String =
    try {
        val inputFormat = SimpleDateFormat("yyyyMMdd", Locale.KOREA)
        val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA)
        val date = inputFormat.parse(dateString)
        outputFormat.format(date!!)
    } catch (e: Exception) {
        dateString
    }
