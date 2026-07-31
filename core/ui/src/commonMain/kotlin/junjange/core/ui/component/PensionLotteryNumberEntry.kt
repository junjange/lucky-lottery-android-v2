package junjange.core.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import junjange.core.designsystem.theme.LottoSpacing
import junjange.core.designsystem.theme.lotteryColors
import junjange.core.domain.model.LottoType
import junjange.core.ui.resources.Res
import junjange.core.ui.resources.add_number
import junjange.core.ui.resources.create_title
import junjange.core.ui.resources.enter_pension_lottery_number
import junjange.core.ui.resources.lotto_entry_games_count
import junjange.core.ui.resources.lotto_entry_max_games
import junjange.core.ui.resources.lotto_entry_remove_game
import junjange.core.ui.resources.pension_entry_group_label
import junjange.core.ui.resources.pension_entry_guide
import org.jetbrains.compose.resources.stringResource

/** 조를 뺀 번호 자리 수. */
private const val DIGITS_PER_GAME = 6

/** 조는 1조부터 5조까지만 발행된다. */
private const val GROUP_COUNT = 5

/**
 * 담은 게임 줄의 볼 크기.
 *
 * 로또(30dp)보다 작다. 한 자리 숫자만 담는 데다 앞에 조 칩까지 붙어서, 같은 크기로 두면
 * 볼 사이 간격을 로또 줄만큼 벌릴 수 없다.
 */
private val CommittedBallSize = 26.dp

/** 로또와 같은 상한. 게임 이름이 알파벳을 벗어나지 않는 26(A~Z)까지 담는다. */
private const val MAX_GAMES = 26

/**
 * 키패드 열 수.
 *
 * 로또는 45개를 넣어야 해서 6열까지 좁혔지만 여기는 열 개뿐이라 좁힐 이유가 없다.
 * 5열이면 360dp 화면에서 한 칸이 (320 − 4×4)/5 = 60.8dp라 48dp 키가 넉넉히 들어간다.
 */
private const val KEYPAD_COLUMNS = 5

/** 로또 격자와 같은 간격을 써서 두 화면의 리듬을 맞춘다. */
private val KeypadGap = LottoSpacing.xs

/**
 * 연금복권 번호 직접 담기.
 *
 * 로또와 같이 눌러서 담는다. 예전에는 조까지 포함한 일곱 칸에 타이핑하는 방식이었고 두 가지가 문제였다.
 * 조 칸이 0~9를 다 받아놓고 저장할 때가 되어서야 "1~5를 입력해주세요"라고 거절했고,
 * iOS 숫자 키패드에는 리턴 키가 없어 키보드를 내릴 방법이 없었다.
 *
 * 조는 1~5 세그먼트로 바꿔 유효하지 않은 값을 고를 수 없게 했고, 여섯 자리는 0~9 키패드로 채운다.
 * 그래서 이 화면에도 키보드가 뜨지 않고, iOS 전용 완료 버튼(`inputAccessoryView`)을 위한
 * `expect/actual`을 새로 뚫을 필요도 없어졌다.
 *
 * 자리를 누르면 커서만 옮겨오고, 키패드를 누르면 그 자리가 바뀐다. 로또와 같은 방식이다.
 * 다만 연금복권 번호는 112233처럼 같은 숫자가 여러 자리에 올 수 있어서, 로또와 달리
 * 중복을 막지 않고 그대로 덮어쓴다.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PensionLotteryNumberEntry(
    onClose: () -> Unit,
    onSubmit: (List<List<String>>) -> Unit,
) {
    // 담기를 마친 게임들. 각 항목은 [조, 여섯 자리] 순서로, 저장 형식과 같게 보관한다.
    val games = remember { mutableStateListOf<List<Int>>() }

    var group by remember { mutableStateOf<Int?>(null) }
    val digits = remember { mutableStateListOf<Int?>(null, null, null, null, null, null) }

    // 다음 숫자가 들어갈 자리.
    var activeIndex by remember { mutableIntStateOf(0) }

    val tapFeedback = rememberNumberTapFeedback()

    val filledCount = digits.count { it != null }
    val isPickingComplete = group != null && filledCount == DIGITS_PER_GAME
    val completeGameCount = games.size + if (isPickingComplete) 1 else 0
    val isFull = completeGameCount >= MAX_GAMES

    fun currentGame(): List<Int> = listOf(group!!) + digits.map { it!! }

    fun resetPicking() {
        group = null
        digits.indices.forEach { digits[it] = null }
        activeIndex = 0
    }

    NumberEntryScaffold(
        title = stringResource(Res.string.enter_pension_lottery_number),
        addLabel = stringResource(Res.string.add_number),
        addEnabled = isPickingComplete && !isFull,
        confirmLabel = stringResource(Res.string.create_title),
        confirmEnabled = completeGameCount > 0,
        // 담아 둔 게임뿐 아니라 고르는 중인 조·자리도 저장 전이므로 함께 센다.
        hasUnsaved = games.isNotEmpty() || group != null || filledCount > 0,
        onClose = onClose,
        onAdd = {
            games.add(currentGame())
            resetPicking()
        },
        // 담기를 누르지 않고 바로 생성해도 지금 고른 게임을 함께 저장한다.
        onConfirm = {
            val result = games + if (isPickingComplete) listOf(currentGame()) else emptyList()
            onSubmit(result.map { numbers -> numbers.map { it.toString() } })
        },
    ) {
        Spacer(modifier = Modifier.height(LottoSpacing.base))

        // 안내는 화면을 열었을 때 가장 먼저 읽히도록 제목 바로 아래에 둔다.
        EntryGuide(
            text =
                if (isFull) {
                    stringResource(Res.string.lotto_entry_max_games)
                } else {
                    stringResource(Res.string.pension_entry_guide)
                },
        )

        Spacer(modifier = Modifier.height(LottoSpacing.xl))

        if (games.isNotEmpty()) {
            EntrySectionLabel(text = stringResource(Res.string.lotto_entry_games_count, games.size))

            games.forEachIndexed { index, numbers ->
                CommittedGameRow(
                    label = gameLabel(index),
                    onRemove = { games.removeAt(index) },
                    // 칩 높이를 옆 볼과 같게 맞춘다.
                    leading = { LottoGroupChip(group = numbers.first().toString(), height = CommittedBallSize) },
                ) {
                    numbers.drop(1).forEachIndexed { digitIndex, digit ->
                        LottoBall(
                            lottoType = LottoType.LOTTO720,
                            lottoColor = lotteryColors[digitIndex + 1],
                            lottoTitle = digit.toString(),
                            size = CommittedBallSize,
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(LottoSpacing.xl))
        }

        EntrySectionLabel(text = stringResource(Res.string.pension_entry_group_label))

        SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
            repeat(GROUP_COUNT) { index ->
                val value = index + 1
                SegmentedButton(
                    selected = group == value,
                    onClick = {
                        group = value
                        tapFeedback(false)
                    },
                    shape = SegmentedButtonDefaults.itemShape(index = index, count = GROUP_COUNT),
                    enabled = !isFull || group == value,
                ) {
                    Text(text = value.toString())
                }
            }
        }

        Spacer(modifier = Modifier.height(LottoSpacing.base))

        PickingSlots(count = DIGITS_PER_GAME) { index ->
            val digit = digits[index]

            PickingSlot(
                isActive = activeIndex == index,
                onClick = { activeIndex = index },
            ) {
                if (digit != null) {
                    LottoBall(
                        lottoType = LottoType.LOTTO720,
                        lottoColor = lotteryColors[index + 1],
                        lottoTitle = digit.toString(),
                        size = EntryBallSize,
                    )
                } else {
                    LottoBallPlaceholder(lottoTitle = "", size = EntryBallSize)
                }
            }
        }

        Spacer(modifier = Modifier.height(LottoSpacing.base))

        DigitKeypad(
            enabled = !isFull,
            onDigit = { digit ->
                // 로또의 writeAt과 달리 맞바꾸지 않는다. 같은 숫자가 여러 자리에 올 수 있다.
                digits[activeIndex] = digit
                activeIndex = digits.indexOfFirst { it == null }.takeIf { it >= 0 } ?: activeIndex
                tapFeedback(group != null && digits.all { it != null })
            },
        )

        Spacer(modifier = Modifier.height(LottoSpacing.base))
    }
}

/** 0~9 키패드. 로또 격자와 같은 칸 크기·같은 간격을 쓴다. */
@Composable
private fun DigitKeypad(
    enabled: Boolean,
    onDigit: (Int) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(KeypadGap)) {
        (0..9).chunked(KEYPAD_COLUMNS).forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(KeypadGap),
            ) {
                row.forEach { digit ->
                    Box(
                        modifier = Modifier.weight(1f).height(LottoSpacing.minTouchTarget),
                        contentAlignment = Alignment.Center,
                    ) {
                        NumberCell(
                            text = digit.toString(),
                            isPicked = false,
                            enabled = enabled,
                            pickedColor = Color.Transparent,
                            onClick = { onDigit(digit) },
                        )
                    }
                }
            }
        }
    }
}
