package junjange.core.ui.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import junjange.core.designsystem.theme.LottoSpacing
import junjange.core.designsystem.theme.toLotteryColor
import junjange.core.domain.model.LottoType
import junjange.core.ui.resources.Res
import junjange.core.ui.resources.add_number
import junjange.core.ui.resources.create_title
import junjange.core.ui.resources.enter_lotto_numbers
import junjange.core.ui.resources.lotto_entry_games_count
import junjange.core.ui.resources.lotto_entry_guide
import junjange.core.ui.resources.lotto_entry_max_games
import junjange.core.ui.resources.lotto_entry_remove_game
import org.jetbrains.compose.resources.stringResource

/** 한 게임에 담기는 번호 개수. */
private const val NUMBERS_PER_GAME = 6

/**
 * 담을 수 있는 게임 수.
 *
 * 실물 용지는 A~E 다섯 게임이지만 여러 장을 이어서 담을 수 있어야 해서 더 열어 둔다.
 * 상한은 게임 이름이 알파벳을 벗어나지 않는 26(A~Z)에서 잡는다.
 */
private const val MAX_GAMES = 26

/**
 * 격자 열 수.
 *
 * 45를 6열로 놓으면 일곱 행이 여섯 개씩 차고 마지막 행에 세 개가 남아 8행이 된다.
 * 위 여섯 자리와 열 수가 같아서 고른 번호가 어느 열에서 왔는지 세로로 이어 보인다.
 *
 * 360dp 화면의 가용 폭 320dp에서 간격 4dp × 5를 빼면 한 칸이 50dp로,
 * 44dp 원을 넣고도 48dp 최소 터치 크기를 가로·세로 모두 지킨다.
 * (8열은 한 칸이 36.5dp로 줄어 가로 기준을 못 지켰다.)
 */
private const val GRID_COLUMNS = 6

/**
 * 담기 화면의 볼 지름.
 *
 * 격자 칸(50dp)과 고른 자리(48dp) 양쪽에서 좌우 여유를 남기는 크기다. 두 줄이 같은 크기를 써야
 * 격자에서 고른 것이 위로 그대로 옮겨간 것으로 읽힌다.
 */
internal val EntryBallSize = 44.dp

/** 격자 한 행의 높이. 원보다 크게 잡아 세로 터치 여유를 남긴다. */
private val GridRowHeight = LottoSpacing.minTouchTarget

/** 6열을 50dp씩 쓰려면 4dp까지 좁혀야 한다. */
private val GridGap = LottoSpacing.xs

/**
 * 로또 번호 직접 담기.
 *
 * 1~45 격자에서 골라 담는다. 예전에는 여섯 칸에 숫자를 타이핑하는 방식이었는데, 그 방식은
 * 두 자리 수 때문에 문제가 겹쳐 있었다. 1~4를 넣으면 10~45가 될 수 있어 그 칸에 머물고
 * 5~9를 넣으면 바로 다음 칸으로 넘어가서, 같은 동작인데 숫자에 따라 결과가 달랐다.
 * 게다가 iOS 숫자 키패드에는 리턴 키가 없어(`KeyboardType.Number` → `UIKeyboardTypeNumberPad`)
 * 다음 칸으로 넘길 방법도, 키보드를 내릴 방법도 없었다.
 *
 * 골라 담는 방식은 그 원인을 없앤다. 키보드가 뜨지 않고, 없는 번호를 고를 수 없어 범위 검증이
 * 필요 없고, 한 게임 안에서 같은 번호를 두 번 가질 수 없어 중복 검증도 필요 없다.
 * 그래서 예전에 있던 `onDuplicateLottery` 콜백이 사라졌다.
 */
@Composable
fun LottoNumberEntry(
    onClose: () -> Unit,
    onSubmit: (List<List<String>>) -> Unit,
) {
    // 담기를 마친 게임들. 고른 순서가 아니라 오름차순으로 보관해 목록·저장 형태를 맞춘다.
    val games = remember { mutableStateListOf<List<Int>>() }

    // 지금 고르고 있는 게임. 커서로 자리를 고쳐야 하므로 자리 수를 고정하고 빈 자리는 null로 둔다.
    val picks = remember { mutableStateListOf<Int?>(null, null, null, null, null, null) }

    // 다음 번호가 들어갈 자리.
    var activeIndex by remember { mutableIntStateOf(0) }

    val tapFeedback = rememberNumberTapFeedback()

    val filledCount = picks.count { it != null }
    val isPickingComplete = filledCount == NUMBERS_PER_GAME
    val completeGameCount = games.size + if (isPickingComplete) 1 else 0
    val isFull = completeGameCount >= MAX_GAMES

    fun resetPicking() {
        picks.indices.forEach { picks[it] = null }
        activeIndex = 0
    }

    fun currentGame(): List<Int> = picks.filterNotNull().sorted()

    NumberEntryScaffold(
        title = stringResource(Res.string.enter_lotto_numbers),
        addLabel = stringResource(Res.string.add_number),
        addEnabled = isPickingComplete && !isFull,
        confirmLabel = stringResource(Res.string.create_title),
        confirmEnabled = completeGameCount > 0,
        // 담아 둔 게임뿐 아니라 고르는 중인 번호도 저장 전이므로 함께 센다.
        hasUnsaved = games.isNotEmpty() || filledCount > 0,
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
                    stringResource(Res.string.lotto_entry_guide)
                },
        )

        Spacer(modifier = Modifier.height(LottoSpacing.xl))

        if (games.isNotEmpty()) {
            EntrySectionLabel(text = stringResource(Res.string.lotto_entry_games_count, games.size))

            games.forEachIndexed { index, numbers ->
                CommittedGameRow(
                    label = gameLabel(index),
                    onRemove = { games.removeAt(index) },
                ) {
                    numbers.forEach { number ->
                        LottoBall(
                            lottoType = LottoType.LOTTO645,
                            lottoColor = number.toLotteryColor(),
                            lottoTitle = number.toString(),
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(LottoSpacing.xl))
        }

        PickingSlots(count = NUMBERS_PER_GAME) { index ->
            val number = picks[index]

            PickingSlot(
                isActive = activeIndex == index,
                onClick = { activeIndex = index },
            ) {
                if (number != null) {
                    LottoBall(
                        lottoType = LottoType.LOTTO645,
                        lottoColor = number.toLotteryColor(),
                        lottoTitle = number.toString(),
                        size = EntryBallSize,
                    )
                } else {
                    LottoBallPlaceholder(lottoTitle = "", size = EntryBallSize)
                }
            }
        }

        Spacer(modifier = Modifier.height(LottoSpacing.base))

        NumberGrid(
            picks = picks,
            enabled = !isFull,
            onNumberTap = { number ->
                writeAt(picks, activeIndex, number)
                activeIndex = picks.indexOfFirst { it == null }.takeIf { it >= 0 } ?: activeIndex
                tapFeedback(picks.count { it != null } == NUMBERS_PER_GAME)
            },
        )

        Spacer(modifier = Modifier.height(LottoSpacing.base))
    }
}

/**
 * 커서 자리에 번호를 넣는다.
 *
 * 누른 번호가 다른 자리에 이미 있으면 두 자리를 맞바꾼다. 그래야 한 게임 안에 같은 번호가
 * 두 번 생기지 않고, 원래 있던 번호도 잃지 않는다. 커서 자리가 비어 있었다면 맞바꾼 결과가
 * 곧 '번호가 그 자리로 옮겨간 것'이 된다.
 */
internal fun writeAt(
    slots: MutableList<Int?>,
    index: Int,
    value: Int,
) {
    val existing = slots.indexOf(value)
    if (existing == index) return

    if (existing >= 0) slots[existing] = slots[index]
    slots[index] = value
}

/** 게임 이름. 실물 용지의 A~E와 같은 방식으로 붙이고, 다섯 게임을 넘으면 F부터 이어 간다. */
internal fun gameLabel(index: Int): String = ('A' + index).toString()

/** 섹션 라벨. 라벨과 그 내용은 4dp로 붙여 한 덩어리로 읽히게 한다. */
@Composable
internal fun EntrySectionLabel(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
    Spacer(modifier = Modifier.height(LottoSpacing.xs))
}

/**
 * 담기를 마친 게임 한 줄.
 *
 * 360dp 기준 폭: 라벨 24 + 간격 8 + (볼 30×6 + 간격 8×5) + 간격 8 + 지우기 48 = 308dp ≤ 320dp.
 * 연금복권은 라벨 대신 40dp 조 칩이 들어가지만 볼이 26dp라 300dp로 더 여유가 있다.
 */
@Composable
internal fun CommittedGameRow(
    label: String,
    onRemove: () -> Unit,
    leading: @Composable (() -> Unit)? = null,
    balls: @Composable () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(LottoSpacing.sm),
    ) {
        if (leading != null) {
            leading()
        } else {
            Text(
                modifier = Modifier.size(24.dp),
                text = label,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
        }

        Row(horizontalArrangement = Arrangement.spacedBy(LottoSpacing.sm)) {
            balls()
        }

        Spacer(modifier = Modifier.weight(1f))

        IconButton(onClick = onRemove) {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = stringResource(Res.string.lotto_entry_remove_game, label),
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

/**
 * 지금 고르고 있는 자리들.
 *
 * 자리 폭을 최소 터치 크기로 못 박고 줄 전체를 가운데로 모은다. 예전에는 남는 폭을 여섯이
 * 나눠 갖게 두고 그 안에 36dp 볼을 넣었는데, 자리가 50dp까지 늘어나 볼 사이가 18dp로 벌어졌다.
 * 아래 격자가 10dp인데 이 줄만 두 배여서 같은 화면 안에서 리듬이 어긋났다.
 *
 * 360dp 기준 폭: 48×6 + 간격 4×5 = 308dp ≤ 320dp. 볼이 44dp라 볼 사이는 8dp가 된다.
 */
@Composable
internal fun PickingSlots(
    count: Int,
    gap: Dp = LottoSpacing.xs,
    slot: @Composable (Int) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(gap, Alignment.CenterHorizontally),
    ) {
        repeat(count) { index ->
            Box(
                modifier = Modifier.size(LottoSpacing.minTouchTarget),
                contentAlignment = Alignment.Center,
            ) {
                slot(index)
            }
        }
    }
}

/**
 * 자리 하나. 눌러도 번호가 빠지지 않고 커서만 옮겨온다.
 *
 * 예전에는 누르면 그 번호가 빠졌는데, 고치려고 눌렀을 때 사라져 버려서 다시 고르게 됐다.
 * 커서를 옮기고 격자에서 새 번호를 누르면 그 자리만 바뀐다. 지우는 것은 지우기 버튼이 맡는다.
 * 볼은 36dp지만 누르는 영역은 48dp이고, 커서는 그 48dp 둘레에 테두리로 표시한다.
 */
@Composable
internal fun PickingSlot(
    isActive: Boolean,
    onClick: () -> Unit,
    content: @Composable () -> Unit,
) {
    Box(
        modifier =
            Modifier
                .size(LottoSpacing.minTouchTarget)
                // 잘라 두지 않으면 리플이 48dp 정사각형으로 번져 안에 든 볼과 모양이 어긋난다.
                .clip(CircleShape)
                .then(
                    if (isActive) {
                        Modifier.border(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.primary,
                            shape = CircleShape,
                        )
                    } else {
                        Modifier
                    },
                ).toggleable(
                    value = isActive,
                    role = Role.RadioButton,
                    onValueChange = { onClick() },
                ),
        contentAlignment = Alignment.Center,
    ) {
        content()
    }
}

/**
 * 사용법 안내.
 *
 * 예전에는 "3개 더 고르면 담을 수 있어요"처럼 남은 개수를 셌다. 그 문구는 번호를 누를 때마다
 * 바뀌어서 계속 읽어야 할 정보처럼 보이는데, 정작 무엇을 어떻게 눌러야 하는지는 알려주지 않았다.
 * 남은 개수는 위 여섯 자리의 빈 칸을 보면 이미 알 수 있다.
 *
 * 이 앱은 이용자 연령이 높은 편이라 고정된 설명을 한 번 읽으면 되는 편이 낫다.
 * 그래서 문구를 바꾸지 않고, 본문 글자 크기(`bodyMedium`)로 키운다.
 *
 * 높이를 고정하지 않는다. 글씨를 키워 쓰는 이용자에게 두 줄이 잘리면 안 되고,
 * 문구가 바뀌지 않으니 높이를 못 박아 흔들림을 막을 이유도 없다.
 */
@Composable
internal fun EntryGuide(text: String) {
    Text(
        modifier = Modifier.fillMaxWidth(),
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}

/** 1~45 격자. 없는 번호를 고를 수 없으니 범위 검증이 필요 없고, 자리를 덮어쓰므로 중복도 생기지 않는다. */
@Composable
private fun NumberGrid(
    picks: List<Int?>,
    enabled: Boolean,
    onNumberTap: (Int) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(GridGap)) {
        (1..45).chunked(GRID_COLUMNS).forEach { row ->
            // 마지막 행은 세 개뿐이다. 남는 자리를 양쪽에 반씩 두어 가운데로 모은다.
            // 한쪽에 몰아 두면 칸 폭은 유지되지만 세 개가 왼쪽에 붙는다.
            val sideWeight = (GRID_COLUMNS - row.size) / 2f

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(GridGap),
            ) {
                if (sideWeight > 0f) Spacer(modifier = Modifier.weight(sideWeight))

                row.forEach { number ->
                    Box(
                        modifier = Modifier.weight(1f).height(GridRowHeight),
                        contentAlignment = Alignment.Center,
                    ) {
                        NumberCell(
                            text = number.toString(),
                            isPicked = picks.contains(number),
                            enabled = enabled,
                            pickedColor = number.toLotteryColor(),
                            size = EntryBallSize,
                            onClick = { onNumberTap(number) },
                        )
                    }
                }

                if (sideWeight > 0f) Spacer(modifier = Modifier.weight(sideWeight))
            }
        }
    }
}

/** 격자 한 칸. 고른 것은 실제 볼 색으로 채워서 위 자리에 담긴 것과 색으로 잇는다. */
@Composable
internal fun NumberCell(
    text: String,
    isPicked: Boolean,
    enabled: Boolean,
    pickedColor: Color,
    onClick: () -> Unit,
    size: Dp = LottoSpacing.minTouchTarget,
) {
    Surface(
        modifier =
            Modifier
                .size(size)
                // Surface에 넘긴 modifier가 Surface 안쪽 clip보다 바깥에 놓여서, 여기서 잘라 두지 않으면
                // 리플이 원을 벗어나 정사각형으로 번진다. 누름 표시를 담을 모양을 먼저 정한다.
                .clip(CircleShape)
                .toggleable(
                    value = isPicked,
                    enabled = enabled,
                    role = Role.Checkbox,
                    onValueChange = { onClick() },
                ),
        shape = CircleShape,
        color = if (isPicked) pickedColor else MaterialTheme.colorScheme.surfaceContainerHigh,
        contentColor = if (isPicked) Color.White else MaterialTheme.colorScheme.onSurface,
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                color =
                    if (enabled) {
                        Color.Unspecified
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.38f)
                    },
            )
        }
    }
}
