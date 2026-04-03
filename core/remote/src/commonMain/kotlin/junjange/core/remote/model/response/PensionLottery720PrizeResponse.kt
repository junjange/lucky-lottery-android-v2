package junjange.core.remote.model.response

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

import junjange.core.data.model.remote.PensionLotteryHomeEntity

/**
 * 연금복권 720+ 당첨 정보 API 응답 모델 (당첨 번호 + 당첨금)
 * API: /pt720/selectPstPt720Info.do?Round={회차}
 */
@Serializable
data class PensionLottery720PrizeResponse(
    @SerialName("resultCode")
    val resultCode: String?,
    @SerialName("resultMessage")
    val resultMessage: String?,
    @SerialName("data")
    val data: PensionLottery720PrizeData?,
)

@Serializable
data class PensionLottery720PrizeData(
    @SerialName("result")
    val result: List<PensionLottery720PrizeItem>?,
)

@Serializable
data class PensionLottery720PrizeItem(
    @SerialName("rnum")
    val rowNumber: Int,
    @SerialName("wnSqNo")
    val winnerSequenceNumber: Int,
    @SerialName("wnAmt")
    val prizeAmount: Long,
    @SerialName("wnBndNo")
    val bondNumber: String?,
    @SerialName("wnRnkVl")
    val rankValue: String,
    @SerialName("psltRflYmd")
    val settlementDate: String,
    @SerialName("psltEpsd")
    val settlementEpisode: Int,
    @SerialName("psltSn")
    val settlementSequence: Int,
    @SerialName("ltGdsCd")
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
        // "yyyyMMdd" -> "yyyy-MM-dd"
        "${dateString.substring(0, 4)}-${dateString.substring(4, 6)}-${dateString.substring(6, 8)}"
    } catch (e: Exception) {
        dateString
    }
