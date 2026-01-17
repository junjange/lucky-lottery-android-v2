package junjange.core.remote.model.response

import com.google.gson.annotations.SerializedName
import junjange.core.data.model.remote.PensionLotteryHomeEntity
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * 연금복권 720+ 당첨 정보 API 응답 모델 (당첨 번호 + 당첨금)
 * API: /pt720/selectPstPt720Info.do?Round={회차}
 */
data class PensionLottery720PrizeResponse(
    @SerializedName("resultCode")
    val resultCode: String?,
    @SerializedName("resultMessage")
    val resultMessage: String?,
    @SerializedName("data")
    val data: PensionLottery720PrizeData?,
)

data class PensionLottery720PrizeData(
    @SerializedName("result")
    val result: List<PensionLottery720PrizeItem>?,
)

data class PensionLottery720PrizeItem(
    @SerializedName("rnum")
    val rowNumber: Int,
    @SerializedName("wnSqNo")
    val winnerSequenceNumber: Int,
    @SerializedName("wnAmt")
    val prizeAmount: Long,
    @SerializedName("wnBndNo")
    val bondNumber: String?,
    @SerializedName("wnRnkVl")
    val rankValue: String,
    @SerializedName("psltRflYmd")
    val settlementDate: String,
    @SerializedName("psltEpsd")
    val settlementEpisode: Int,
    @SerializedName("psltSn")
    val settlementSequence: Int,
    @SerializedName("ltGdsCd")
    val lotteryGoodsCode: String,
)

/**
 * 연금복권 720+ 응답에서 당첨 번호 정보 추출
 * 요청한 회차의 데이터만 추출 (응답에는 여러 회차가 포함될 수 있음)
 */
internal fun PensionLottery720PrizeResponse.toData(requestedRound: Int): PensionLotteryHomeEntity? {
    // 요청한 회차의 데이터만 필터링
    val roundData = data?.result?.filter { it.settlementEpisode == requestedRound } ?: return null
    if (roundData.isEmpty()) return null

    // wnSqNo 1: 1등 당첨 번호 (조 번호 포함)
    val firstPrizeItem = roundData.firstOrNull { it.winnerSequenceNumber == 1 } ?: return null

    // wnSqNo 21: 2등 보너스 번호
    val bonusItem = roundData.firstOrNull { it.winnerSequenceNumber == 21 } ?: return null

    // wnBndNo: 조 (1~5)
    val lotteryGroup = firstPrizeItem.bondNumber?.toIntOrNull() ?: return null

    // wnRnkVl: 6자리 당첨 번호 (예: "960211")
    val winningNumbers = firstPrizeItem.rankValue.padStart(6, '0')
    if (winningNumbers.length != 6) return null

    val bonusNumbers = bonusItem.rankValue.padStart(6, '0')
    if (bonusNumbers.length != 6) return null

    // psltRflYmd: 추첨일 (예: "20260115") -> "2026-01-15"
    val drawDate = formatDrawDate(firstPrizeItem.settlementDate)

    return PensionLotteryHomeEntity(
        round = firstPrizeItem.settlementEpisode,
        winningDate = drawDate,
        lotteryGroup = lotteryGroup,
        winningFirstNum = winningNumbers[0].digitToInt(),
        winningSecondNum = winningNumbers[1].digitToInt(),
        winningThirdNum = winningNumbers[2].digitToInt(),
        winningFourthNum = winningNumbers[3].digitToInt(),
        winningFifthNum = winningNumbers[4].digitToInt(),
        winningSixthNum = winningNumbers[5].digitToInt(),
        bonusFirstNum = bonusNumbers[0].digitToInt(),
        bonusSecondNum = bonusNumbers[1].digitToInt(),
        bonusThirdNum = bonusNumbers[2].digitToInt(),
        bonusFourthNum = bonusNumbers[3].digitToInt(),
        bonusFifthNum = bonusNumbers[4].digitToInt(),
        bonusSixthNum = bonusNumbers[5].digitToInt(),
    )
}

private fun formatDrawDate(dateString: String): String =
    try {
        val inputFormat = SimpleDateFormat("yyyyMMdd", Locale.KOREA)
        val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA)
        val date = inputFormat.parse(dateString)
        outputFormat.format(date!!)
    } catch (e: Exception) {
        dateString
    }
