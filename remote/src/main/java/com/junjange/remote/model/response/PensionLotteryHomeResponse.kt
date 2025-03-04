package com.junjange.remote.model.response

import com.junjange.data.model.remote.PensionLotteryHomeEntity
import okhttp3.ResponseBody
import org.jsoup.Jsoup
import java.text.SimpleDateFormat
import java.util.Locale

data class PensionLotteryHomeResponse(
    val round: Int,
    val winningDate: String,
    val lotteryGroup: Int,
    val winningFirstNum: Int,
    val winningSecondNum: Int,
    val winningThirdNum: Int,
    val winningFourthNum: Int,
    val winningFifthNum: Int,
    val winningSixthNum: Int,
    val bonusFirstNum: Int,
    val bonusSecondNum: Int,
    val bonusThirdNum: Int,
    val bonusFourthNum: Int,
    val bonusFifthNum: Int,
    val bonusSixthNum: Int,
)

internal fun PensionLotteryHomeResponse.toData() =
    PensionLotteryHomeEntity(
        round = round,
        winningDate = winningDate,
        lotteryGroup = lotteryGroup,
        winningFirstNum = winningFirstNum,
        winningSecondNum = winningSecondNum,
        winningThirdNum = winningThirdNum,
        winningFourthNum = winningFourthNum,
        winningFifthNum = winningFifthNum,
        winningSixthNum = winningSixthNum,
        bonusFirstNum = bonusFirstNum,
        bonusSecondNum = bonusSecondNum,
        bonusThirdNum = bonusThirdNum,
        bonusFourthNum = bonusFourthNum,
        bonusFifthNum = bonusFifthNum,
        bonusSixthNum = bonusSixthNum,
    )

internal fun ResponseBody.toParseLotteryNumbers(): PensionLotteryHomeEntity {
    this.use { body ->
        val htmlContent = body.string()
        val document = Jsoup.parse(htmlContent)

        val round =
            document
                .select("div[class='win_result al720'] h4 strong")
                .text()
                .split("회")
                .first()
                .toInt()
        val drawDateText = document.select("div[class='win_result al720'] p").text()

        val drawDate = formatDate(drawDateText)

        val winningNumbersText = document.select("div[class='win_result al720'] span span").text()

        val (winningNumbers, bonusNumbers) =
            winningNumbersText.split("각").let {
                it[0].trim().split(" ").map { it.toInt() } to
                    it[1]
                        .trim()
                        .split(" ")
                        .map { it.toInt() }
            }

        return PensionLotteryHomeEntity(
            round = round,
            winningDate = drawDate,
            lotteryGroup = winningNumbers[0],
            winningFirstNum = winningNumbers[1],
            winningSecondNum = winningNumbers[2],
            winningThirdNum = winningNumbers[3],
            winningFourthNum = winningNumbers[4],
            winningFifthNum = winningNumbers[5],
            winningSixthNum = winningNumbers[6],
            bonusFirstNum = bonusNumbers[0],
            bonusSecondNum = bonusNumbers[1],
            bonusThirdNum = bonusNumbers[2],
            bonusFourthNum = bonusNumbers[3],
            bonusFifthNum = bonusNumbers[4],
            bonusSixthNum = bonusNumbers[5],
        )
    }
}

internal fun formatDate(drawDateText: String): String {
    val inputFormat = SimpleDateFormat("(yyyy년 MM월 dd일 추첨) ", Locale.KOREA)
    val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA)
    val date = inputFormat.parse(drawDateText)
    return outputFormat.format(date)
}
