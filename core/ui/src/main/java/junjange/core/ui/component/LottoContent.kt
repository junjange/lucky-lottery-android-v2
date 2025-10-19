package junjange.core.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import junjange.core.designsystem.theme.LottoTheme
import junjange.core.designsystem.theme.lotteryColors
import junjange.core.designsystem.theme.toLotteryColor
import junjange.core.domain.model.LotteryNumbers
import junjange.core.domain.model.LottoType
import junjange.core.domain.model.PensionLotteryHome
import junjange.core.domain.model.WinningLotteryNumbers
import junjange.core.domain.model.WinningPensionLotteryBonusNumbers
import junjange.core.domain.model.WinningPensionLotteryNumbers
import junjange.core.ui.R
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun LottoContent(
    lotteryNumbers: LotteryNumbers?,
    pensionLotteryHome: PensionLotteryHome?,
    changeLottery: (offset: Int) -> Unit,
    changePensionLottery: (offset: Int) -> Unit,
) {
    lotteryNumbers?.let {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(
                onClick = { changeLottery(-1) },
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_chevron_left),
                    contentDescription = null,
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(modifier = Modifier.height(20.dp))
                LottoContentTitle(
                    title = stringResource(R.string.lotto_645_title),
                    round = lotteryNumbers.round,
                    winningDate = lotteryNumbers.winningDate,
                )
                Lotto645Content(lotteryNumbers = lotteryNumbers)
                LottoPrizeCard(
                    prizeAmount = lotteryNumbers.prizeAmount,
                    winnerCount = lotteryNumbers.winnerCount,
                    perPersonAmount = lotteryNumbers.perPersonAmount,
                )
            }

            IconButton(
                onClick = { changeLottery(+1) },
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_chevron_right),
                    contentDescription = null,
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }

    pensionLotteryHome?.let {
        Spacer(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(0.5.dp)
                    .padding(
                        start = 20.dp,
                        end = 20.dp,
                    ).background(color = LottoTheme.colors.gray400),
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(
                onClick = { changePensionLottery(-1) },
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_chevron_left),
                    contentDescription = null,
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(modifier = Modifier.height(20.dp))
                LottoContentTitle(
                    title = stringResource(R.string.lotto_720_title),
                    round = pensionLotteryHome.round,
                    winningDate = pensionLotteryHome.winningDate,
                )
                Lotto720Content(pensionLotteryHome = pensionLotteryHome)
            }

            IconButton(
                onClick = { changePensionLottery(+1) },
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_chevron_right),
                    contentDescription = null,
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun LottoContentTitle(
    title: String,
    round: Int,
    winningDate: String?,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = title,
            style = LottoTheme.typography.headline3,
        )
        Spacer(modifier = Modifier.height(15.dp))
        Text(
            text = "${round}회 당첨결과",
            style = LottoTheme.typography.body1,
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = "(${winningDate.parseDateToKoreanFormat()} 추첨)",
            style = LottoTheme.typography.body3,
        )
        Spacer(modifier = Modifier.height(20.dp))
    }
}

fun String?.parseDateToKoreanFormat(): String {
    this ?: return "미발표"
    val inputFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val outputFormatter = SimpleDateFormat("yyyy년 MM월 dd일", Locale.getDefault())

    val parsedDateTime = inputFormatter.parse(this)
    return parsedDateTime?.let { outputFormatter.format(it) } ?: run { "잘못된 날짜" }
}

@Composable
fun Lotto645Content(lotteryNumbers: LotteryNumbers) {
    val lotteryContent =
        listOf(
            lotteryNumbers.firstNum,
            lotteryNumbers.secondNum,
            lotteryNumbers.thirdNum,
            lotteryNumbers.fourthNum,
            lotteryNumbers.fifthNum,
            lotteryNumbers.sixthNum,
            lotteryNumbers.bonusNum,
        )

    Row(
        modifier = Modifier.padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        lotteryContent.forEachIndexed { index, s ->
            val color = s.toLotteryColor()

            if (index == 6) {
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    modifier = Modifier.size(14.dp),
                    painter = painterResource(id = R.drawable.ic_plus),
                    contentDescription = null,
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            LottoBall(
                lottoType = LottoType.LOTTO645,
                lottoColor = color,
                lottoTitle = s.toString(),
            )
            Spacer(modifier = Modifier.width(4.dp))
        }
    }
    Spacer(modifier = Modifier.height(5.dp))
    Text(
        text = stringResource(R.string.winning_numbers_title),
        style = LottoTheme.typography.body3,
    )
}

@Composable
fun LottoPrizeCard(
    prizeAmount: Long,
    winnerCount: Int,
    perPersonAmount: Long,
) {
    Column(
        modifier =
            Modifier
                .padding(16.dp)
                .clip(RoundedCornerShape(8.dp)),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "1등 총 당첨금",
            fontSize = 16.sp,
        )
        Text(
            text = "${prizeAmount.formatPrizeAmount()}원",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = "(${winnerCount}명 / ${perPersonAmount.formatPrizeAmount()})",
            fontSize = 14.sp,
        )
    }
}

@Composable
fun Lotto645Content(winningLotteryNumbers: WinningLotteryNumbers) {
    val lotteryContent =
        listOf(
            winningLotteryNumbers.firstNum,
            winningLotteryNumbers.secondNum,
            winningLotteryNumbers.thirdNum,
            winningLotteryNumbers.fourthNum,
            winningLotteryNumbers.fifthNum,
            winningLotteryNumbers.sixthNum,
            winningLotteryNumbers.bonusNum,
        )

    Row(
        modifier = Modifier.padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        lotteryContent.forEachIndexed { index, s ->
            val color = s.toLotteryColor()

            if (index == 6) {
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    modifier = Modifier.size(14.dp),
                    painter = painterResource(id = R.drawable.ic_plus),
                    contentDescription = null,
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            LottoBall(
                lottoType = LottoType.LOTTO645,
                lottoColor = color,
                lottoTitle = s.toString(),
            )
            Spacer(modifier = Modifier.width(4.dp))
        }
    }
    Spacer(modifier = Modifier.height(5.dp))
    Text(
        text = stringResource(R.string.winning_numbers_title),
        style = LottoTheme.typography.body3,
    )
}

@Composable
fun Lotto720Content(pensionLotteryHome: PensionLotteryHome) {
    val pensionLotteryContent =
        listOf(
            LottoTheme.colors.lottoGray to pensionLotteryHome.lotteryGroup,
            LottoTheme.colors.lottoError to pensionLotteryHome.winningFirstNum,
            LottoTheme.colors.lottoOrange to pensionLotteryHome.winningSecondNum,
            LottoTheme.colors.lottoYellow to pensionLotteryHome.winningThirdNum,
            LottoTheme.colors.lottoBlue to pensionLotteryHome.winningFourthNum,
            LottoTheme.colors.lottoPurple to pensionLotteryHome.winningFifthNum,
            LottoTheme.colors.lottoBlack to pensionLotteryHome.winningSixthNum,
        )

    val pensionBonusLotteryContent =
        listOf(
            LottoTheme.colors.lottoGray to "각",
            LottoTheme.colors.lottoError to pensionLotteryHome.bonusFirstNum,
            LottoTheme.colors.lottoOrange to pensionLotteryHome.bonusSecondNum,
            LottoTheme.colors.lottoYellow to pensionLotteryHome.bonusThirdNum,
            LottoTheme.colors.lottoBlue to pensionLotteryHome.bonusFourthNum,
            LottoTheme.colors.lottoPurple to pensionLotteryHome.bonusFifthNum,
            LottoTheme.colors.lottoBlack to pensionLotteryHome.bonusSixthNum,
        )

    Row(
        modifier = Modifier.padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        pensionLotteryContent.forEachIndexed { index, s ->
            if (index == 1) {
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = stringResource(id = R.string.group_title),
                    style = LottoTheme.typography.headline3,
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            LottoBall(
                lottoType = LottoType.LOTTO720,
                lottoColor = s.first,
                lottoTitle = s.second.toString(),
            )
            Spacer(modifier = Modifier.width(4.dp))
        }
    }
    Spacer(modifier = Modifier.height(5.dp))
    Text(
        text = stringResource(R.string.winning_numbers_title),
        style = LottoTheme.typography.body3,
    )
    Spacer(modifier = Modifier.height(20.dp))
    Row(
        modifier = Modifier.padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        pensionBonusLotteryContent.forEachIndexed { index, s ->
            if (index == 1) {
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = stringResource(id = R.string.group_title),
                    style = LottoTheme.typography.headline3,
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            LottoBall(
                lottoType = LottoType.LOTTO720,
                lottoColor = s.first,
                lottoTitle = s.second.toString(),
            )
            Spacer(modifier = Modifier.width(4.dp))
        }
    }
    Spacer(modifier = Modifier.height(5.dp))
    Text(
        text = stringResource(R.string.bonus_numbers_title),
        style = LottoTheme.typography.body3,
    )
}

@Composable
fun Lotto720Content(
    winningPensionLotteryNumbers: WinningPensionLotteryNumbers,
    winningPensionLotteryBonusNumbers: WinningPensionLotteryBonusNumbers,
) {
    val winningLotteryNumbersTitle =
        listOf(
            winningPensionLotteryNumbers.group,
            winningPensionLotteryNumbers.firstNum,
            winningPensionLotteryNumbers.secondNum,
            winningPensionLotteryNumbers.thirdNum,
            winningPensionLotteryNumbers.fourthNum,
            winningPensionLotteryNumbers.fifthNum,
            winningPensionLotteryNumbers.sixthNum,
        ).map { it.toString() }

    val winningPensionLotteryBonusNumbersTitle =
        listOf(
            "각",
            winningPensionLotteryBonusNumbers.firstNum,
            winningPensionLotteryBonusNumbers.secondNum,
            winningPensionLotteryBonusNumbers.thirdNum,
            winningPensionLotteryBonusNumbers.fourthNum,
            winningPensionLotteryBonusNumbers.fifthNum,
            winningPensionLotteryBonusNumbers.sixthNum,
        ).map { it.toString() }

    Row(
        modifier = Modifier.padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        winningLotteryNumbersTitle.forEachIndexed { index, title ->
            if (index == 1) {
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = stringResource(id = R.string.group_title),
                    style = LottoTheme.typography.headline3,
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            LottoBall(
                lottoType = LottoType.LOTTO720,
                lottoColor = lotteryColors[index],
                lottoTitle = title,
            )
            Spacer(modifier = Modifier.width(4.dp))
        }
    }
    Spacer(modifier = Modifier.height(5.dp))
    Text(
        text = stringResource(R.string.winning_numbers_title),
        style = LottoTheme.typography.body3,
    )
    Spacer(modifier = Modifier.height(20.dp))
    Row(
        modifier = Modifier.padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        winningPensionLotteryBonusNumbersTitle.forEachIndexed { index, title ->
            if (index == 1) {
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = stringResource(id = R.string.group_title),
                    style = LottoTheme.typography.headline3,
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            LottoBall(
                lottoType = LottoType.LOTTO720,
                lottoColor = lotteryColors[index],
                lottoTitle = title,
            )
            Spacer(modifier = Modifier.width(4.dp))
        }
    }
    Spacer(modifier = Modifier.height(5.dp))
    Text(
        text = stringResource(R.string.bonus_numbers_title),
        style = LottoTheme.typography.body3,
    )
}

private fun Long.formatPrizeAmount(): String =
    when {
        this >= 100_000_000 -> "${this / 100_000_000}억"
        this >= 10_000 -> "${this / 10_000}만원"
        else -> "${this}원"
    }
