package junjange.core.remote.model.response

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

import junjange.core.data.model.remote.LottoEntity

/**
 * 로또 645 당첨 정보 API 응답 모델
 * API: /lt645/selectPstLt645Info.do?srchLtEpsd={회차}
 */
@Serializable
data class Lotto645InfoResponse(
    @SerialName("resultCode")
    val resultCode: String?,
    @SerialName("resultMessage")
    val resultMessage: String?,
    @SerialName("data")
    val data: Lotto645Data?,
)

@Serializable
data class Lotto645Data(
    @SerialName("list")
    val list: List<Lotto645Item>?,
)

@Serializable
data class Lotto645Item(
    @SerialName("ltEpsd")
    val round: Int,
    @SerialName("tm1WnNo")
    val number1: Int,
    @SerialName("tm2WnNo")
    val number2: Int,
    @SerialName("tm3WnNo")
    val number3: Int,
    @SerialName("tm4WnNo")
    val number4: Int,
    @SerialName("tm5WnNo")
    val number5: Int,
    @SerialName("tm6WnNo")
    val number6: Int,
    @SerialName("bnsWnNo")
    val bonusNumber: Int,
    @SerialName("ltRflYmd")
    val drawDate: String,
    @SerialName("rnk1WnNope")
    val rank1WinnerCount: Int,
    @SerialName("rnk1WnAmt")
    val rank1PrizeAmount: Long,
    @SerialName("rnk1SumWnAmt")
    val rank1TotalAmount: Long,
    @SerialName("wholEpsdSumNtslAmt")
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
        // "yyyyMMdd" -> "yyyy-MM-dd"
        "${dateString.substring(0, 4)}-${dateString.substring(4, 6)}-${dateString.substring(6, 8)}"
    } catch (e: Exception) {
        dateString
    }
