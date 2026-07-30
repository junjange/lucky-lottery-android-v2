package junjange.core.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import junjange.core.designsystem.theme.LottoBlack
import junjange.core.designsystem.theme.LottoBlue
import junjange.core.designsystem.theme.LottoError
import junjange.core.designsystem.theme.LottoOrange
import junjange.core.designsystem.theme.LottoPurple
import junjange.core.designsystem.theme.LottoShapeTokens
import junjange.core.designsystem.theme.LottoShapes
import junjange.core.designsystem.theme.LottoSpacing
import junjange.core.designsystem.theme.LottoYellow
import junjange.core.designsystem.theme.toLotteryColor
import junjange.core.domain.model.LotteryNumbers
import junjange.core.domain.model.LottoType
import junjange.core.domain.model.PensionLotteryHome
import junjange.core.ui.resources.*
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

// ---------------------------------------------------------------------------
// 번호를 보여주는 화면들이 공유하는 조각. 홈·내 번호가 같은 규칙을 쓰도록 여기에 모은다.
// ---------------------------------------------------------------------------

/** 연금복권 자리별 색. 실물 용지의 자리 색을 그대로 쓴다. */
val pensionBallColors =
    listOf(LottoError, LottoOrange, LottoYellow, LottoBlue, LottoPurple, LottoBlack)

/**
 * 조 칩 폭. 당첨행("1조")과 보너스행("각조")의 글자 폭이 달라도 아래위 볼이 같은 x에서 시작하도록
 * 폭을 고정한다. 폭을 내용에 맡기면 두 줄의 볼 간격과 시작 위치가 서로 어긋난다.
 */
private val GroupChipWidth = 44.dp

/**
 * 번호 한 줄과 그 아래 이름표.
 *
 * 이름표를 번호 위가 아니라 아래에 둔다. 번호가 먼저 눈에 들어오고 무엇인지는 그 다음에
 * 확인하는 순서가 자연스럽다. 줄은 가운데로 모아 어느 회차를 넘겨도 번호가 같은 자리에 온다.
 *
 * 볼 크기와 간격은 dp 고정이라 글자 크기 설정에 커지지 않는다. 폭이 늘어날 수 있는 것은
 * 이름표뿐인데 볼 줄보다 짧아서 넘칠 일이 없다.
 */
@Composable
fun LottoNumberSection(
    label: String,
    modifier: Modifier = Modifier,
    spacing: Dp = LottoSpacing.sm,
    balls: @Composable RowScope.() -> Unit,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(spacing),
            content = balls,
        )
        Spacer(modifier = Modifier.height(LottoSpacing.sm))
        NumberCaption(text = label)
    }
}

/** 번호 아래 이름표. 번호를 읽는 데 방해되지 않도록 작고 흐리게 둔다. */
@Composable
private fun NumberCaption(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        maxLines = 1,
    )
}

/**
 * 6/45 당첨번호 여섯 개 + 보너스.
 *
 * 보너스는 당첨 판정 규칙이 다르므로 줄을 나누지 않고 '+'로 이어 붙이되 이름표는 따로 준다.
 * 이름표가 각 무리 아래에 놓여 어디까지가 당첨번호이고 어디부터가 보너스인지 선을 긋는다.
 *
 * 볼은 [LottoBallDefaultSize]를 쓴다. 여섯 개 + '+' + 보너스 + 이름표가 한 줄에 들어가야 해서
 * 카드 폭에 여유를 남기려면 이 크기가 상한이다.
 */
@Composable
fun Lotto645WinningSection(
    numbers: List<Int>,
    bonus: Int?,
    modifier: Modifier = Modifier,
    size: Dp = LottoBallDefaultSize,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.Top,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row(horizontalArrangement = Arrangement.spacedBy(LottoSpacing.xs)) {
                numbers.forEach { number ->
                    LottoBall(
                        lottoType = LottoType.LOTTO645,
                        lottoColor = number.toLotteryColor(),
                        lottoTitle = number.toString(),
                        size = size,
                    )
                }
            }
            Spacer(modifier = Modifier.height(LottoSpacing.sm))
            NumberCaption(text = stringResource(Res.string.winning_numbers_title))
        }

        bonus?.let {
            // '+'는 이름표까지 포함한 열의 가운데가 아니라 볼 높이의 가운데에 와야 한다.
            Box(
                modifier =
                    Modifier
                        .height(size)
                        .padding(horizontal = LottoSpacing.xs),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    modifier = Modifier.size(14.dp),
                    painter = painterResource(Res.drawable.ic_plus),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                LottoBall(
                    lottoType = LottoType.LOTTO645,
                    lottoColor = it.toLotteryColor(),
                    lottoTitle = it.toString(),
                    size = size,
                )
                Spacer(modifier = Modifier.height(LottoSpacing.sm))
                NumberCaption(text = stringResource(Res.string.bonus_numbers_title))
            }
        }
    }
}

/**
 * 조 칩 + 연금복권 번호 여섯 개.
 *
 * 6/45보다 볼이 하나 많고 앞에 칩까지 붙어서 폭이 가장 빠듯한 행이다.
 * 볼 사이는 6/45보다 좁게 두고, 칩과 첫 볼 사이는 바깥 [LottoNumberSection]의 간격이 맡는다.
 */
@Composable
fun RowScope.LottoPensionBalls(
    group: String,
    numbers: List<String>,
    size: Dp = LottoBallMediumSize,
) {
    LottoGroupChip(group = group, height = size)
    Row(
        horizontalArrangement = Arrangement.spacedBy(LottoSpacing.xs),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        numbers.forEachIndexed { index, number ->
            LottoBall(
                lottoType = LottoType.LOTTO720,
                lottoColor = pensionBallColors[index],
                lottoTitle = number,
                size = size,
            )
        }
    }
}

/**
 * 조 번호.
 *
 * 1등은 조와 여섯 자리가 모두 맞아야 하므로 숫자와 같은 비중(같은 높이, 같은 글자 크기)으로 두고,
 * 원형이 아닌 사각으로 형태만 달리해서 다른 종류의 값임을 보인다.
 * '조'를 번호에 붙여 한 단어로 읽히게 하는 것이 이 칩의 목적이다.
 */
@Composable
fun LottoGroupChip(
    group: String,
    height: Dp = LottoBallMediumSize,
) {
    Surface(
        modifier =
            Modifier
                .width(GroupChipWidth)
                .height(height),
        shape = LottoShapes.small,
        color = MaterialTheme.colorScheme.surfaceContainer,
        contentColor = MaterialTheme.colorScheme.onSurface,
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = group + stringResource(Res.string.group_title),
                style = MaterialTheme.typography.titleSmall,
                maxLines = 1,
            )
        }
    }
}

/**
 * 회차 카드 껍데기. 회색 캔버스 위의 흰 카드 — 테두리나 그림자 없이 명도 차이로만 띄운다.
 *
 * [trailing]에 회차 이동 버튼처럼 제목 오른쪽에 붙는 것을 넣는다.
 */
@Composable
fun LotteryCard(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    trailing: @Composable (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = LottoShapeTokens.card,
        color = MaterialTheme.colorScheme.surfaceContainerLowest,
    ) {
        Column(modifier = Modifier.padding(LottoSpacing.lg)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    Spacer(modifier = Modifier.height(LottoSpacing.xxs))
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                trailing?.invoke()
            }

            Spacer(modifier = Modifier.height(LottoSpacing.lg))
            content()
        }
    }
}

fun String?.parseDateToKoreanFormat(): String {
    this ?: return "미발표"
    val parts = this.split("-")
    if (parts.size != 3) return "잘못된 날짜"
    // API는 07월처럼 두 자리로 주지만 한국어 날짜는 앞의 0을 빼고 읽는다.
    val (year, month, day) = parts
    return "${year}년 ${month.stripLeadingZero()}월 ${day.stripLeadingZero()}일"
}

private fun String.stripLeadingZero(): String = trimStart('0').ifEmpty { this }

// ---------------------------------------------------------------------------
// 홈
// ---------------------------------------------------------------------------

/**
 * 홈의 당첨결과 목록.
 *
 * 복권 종류마다 카드 하나를 쌓고, 카드 안에서만 회차를 넘긴다.
 * 화면 전체를 감싸던 좌우 화살표를 카드 헤더로 옮겨서 어느 카드의 회차가 바뀌는지 분명해진다.
 */
@Composable
fun LottoContent(
    lotteryNumbers: LotteryNumbers?,
    pensionLotteryHome: PensionLotteryHome?,
    changeLottery: (offset: Int) -> Unit,
    changePensionLottery: (offset: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(horizontal = LottoSpacing.screenHorizontal),
        verticalArrangement = Arrangement.spacedBy(LottoSpacing.base),
    ) {
        lotteryNumbers?.let { numbers ->
            LotteryCard(
                title = stringResource(Res.string.lotto_645_title),
                subtitle = lotteryRoundSubtitle(numbers.round, numbers.winningDate),
                trailing = { RoundStepper(onRoundChange = changeLottery) },
            ) {
                Lotto645WinningSection(
                    numbers =
                        listOf(
                            numbers.firstNum,
                            numbers.secondNum,
                            numbers.thirdNum,
                            numbers.fourthNum,
                            numbers.fifthNum,
                            numbers.sixthNum,
                        ),
                    bonus = numbers.bonusNum,
                )

                Spacer(modifier = Modifier.height(LottoSpacing.lg))
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                Spacer(modifier = Modifier.height(LottoSpacing.base))

                PrizeSummary(
                    prizeAmount = numbers.prizeAmount,
                    winnerCount = numbers.winnerCount,
                    perPersonAmount = numbers.perPersonAmount,
                )
            }
        }

        pensionLotteryHome?.let { pension ->
            LotteryCard(
                title = stringResource(Res.string.lotto_720_title),
                subtitle = lotteryRoundSubtitle(pension.round, pension.winningDate),
                trailing = { RoundStepper(onRoundChange = changePensionLottery) },
            ) {
                LottoNumberSection(label = stringResource(Res.string.winning_numbers_title)) {
                    LottoPensionBalls(
                        group = pension.lotteryGroup.toString(),
                        numbers =
                            listOf(
                                pension.winningFirstNum,
                                pension.winningSecondNum,
                                pension.winningThirdNum,
                                pension.winningFourthNum,
                                pension.winningFifthNum,
                                pension.winningSixthNum,
                            ).map { it.toString() },
                    )
                }

                Spacer(modifier = Modifier.height(LottoSpacing.base))

                LottoNumberSection(label = stringResource(Res.string.bonus_numbers_title)) {
                    LottoPensionBalls(
                        group = stringResource(Res.string.pension_bonus_group_all),
                        numbers =
                            listOf(
                                pension.bonusFirstNum,
                                pension.bonusSecondNum,
                                pension.bonusThirdNum,
                                pension.bonusFourthNum,
                                pension.bonusFifthNum,
                                pension.bonusSixthNum,
                            ).map { it.toString() },
                    )
                }
            }
        }
    }
}

/** "1234회 · 2026년 7월 25일 추첨" 형태의 카드 부제목. 추첨 전이면 날짜 대신 그렇게 적는다. */
@Composable
fun lotteryRoundSubtitle(
    round: Int,
    winningDate: String?,
): String =
    if (winningDate == null) {
        stringResource(Res.string.round_unannounced, round)
    } else {
        stringResource(Res.string.round_with_date, round, winningDate.parseDateToKoreanFormat())
    }

@Composable
private fun RoundStepper(onRoundChange: (offset: Int) -> Unit) {
    Row(horizontalArrangement = Arrangement.spacedBy(LottoSpacing.sm)) {
        RoundStepButton(
            icon = Res.drawable.ic_chevron_left,
            description = stringResource(Res.string.previous_round),
            onClick = { onRoundChange(-1) },
        )
        RoundStepButton(
            icon = Res.drawable.ic_chevron_right,
            description = stringResource(Res.string.next_round),
            onClick = { onRoundChange(+1) },
        )
    }
}

/** 회차 이동 버튼. OS 기본 IconButton에 뉴트럴 컨테이너 색만 준다. */
@Composable
private fun RoundStepButton(
    icon: DrawableResource,
    description: String,
    onClick: () -> Unit,
) {
    FilledIconButton(
        onClick = onClick,
        modifier = Modifier.size(36.dp),
        colors =
            IconButtonDefaults.filledIconButtonColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainer,
                contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            ),
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = description,
            modifier = Modifier.size(18.dp),
        )
    }
}

/** 1등 당첨금. 화면에서 가장 큰 숫자로 두고 나머지는 회색 보조 정보로 내린다. */
@Composable
private fun PrizeSummary(
    prizeAmount: Long,
    winnerCount: Int,
    perPersonAmount: Long,
) {
    Column {
        Text(
            text = stringResource(Res.string.first_prize_total),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(modifier = Modifier.height(LottoSpacing.xs))
        Text(
            text = prizeAmount.formatPrizeAmount(),
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Spacer(modifier = Modifier.height(LottoSpacing.xs))
        Text(
            text =
                stringResource(
                    Res.string.first_prize_detail,
                    winnerCount,
                    perPersonAmount.formatPrizeAmount(),
                ),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

// 단위를 붙인 뒤 항상 '원'으로 끝나게 한다.
private fun Long.formatPrizeAmount(): String =
    when {
        this >= 100_000_000 -> "${this / 100_000_000}억원"
        this >= 10_000 -> "${this / 10_000}만원"
        else -> "${this}원"
    }
