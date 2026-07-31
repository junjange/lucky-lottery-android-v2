package junjange.feature.randomnumber

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import junjange.core.designsystem.theme.LottoShapeTokens
import junjange.core.designsystem.theme.LottoSpacing
import junjange.core.ui.component.LottoLargeTitle
import junjange.core.domain.model.LottoType
import junjange.feature.randomnumber.RandomNumberContract.*
import junjange.feature.randomnumber.resources.Res
import junjange.feature.randomnumber.resources.ic_lotto645_random
import junjange.feature.randomnumber.resources.ic_lotto720_random
import junjange.feature.randomnumber.resources.lotto_645_random_description
import junjange.feature.randomnumber.resources.lotto_645_random_title
import junjange.feature.randomnumber.resources.lotto_720_random_description
import junjange.feature.randomnumber.resources.lotto_720_random_title
import junjange.feature.randomnumber.resources.random_number_generation
import junjange.feature.randomnumber.resources.random_number_guide
import kotlinx.coroutines.flow.collectLatest
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

/** 카드 안 일러스트 크기. 카드가 내용 높이를 따르므로 이 값이 카드 높이를 정한다. */
private val CardImageSize = 72.dp

/**
 * 랜덤 번호 탭. 어떤 복권의 번호를 만들지 고르는 두 갈래 화면이다.
 *
 * 예전에는 카드 두 개가 각각 `weight(0.5f)`로 화면 절반씩 차지했다. 내용은 일러스트 하나와
 * 두 줄 텍스트뿐인데 카드 하나가 300dp쯤 되어 빈 공간이 절반을 넘었다.
 * 카드를 내용 높이에 맡기고 위에서부터 쌓는다.
 */
@Composable
fun RandomNumberScreen(
    viewModel: RandomNumberViewModel,
    navigateRandomNumberGeneration: (lottoType: LottoType) -> Unit,
) {
    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is Effect.NavigateToRandomNumberGeneration -> navigateRandomNumberGeneration(effect.lottoType)
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            // 탭 루트라 큰 제목을 쓴다. 뒤로 가기가 있는 번호 생성 화면은 M3 앱바를 그대로 둔다.
            LottoLargeTitle(title = stringResource(Res.string.random_number_generation))

            Column(
                modifier =
                    Modifier
                        // 다른 화면과 같은 좌우 여백을 쓴다. 예전에는 8dp가 두 겹으로 16dp였고
                        // 탭을 옮길 때마다 좌우가 흔들렸다.
                        .padding(horizontal = LottoSpacing.screenHorizontal),
                verticalArrangement = Arrangement.spacedBy(LottoSpacing.md),
            ) {
                Text(
                    text = stringResource(Res.string.random_number_guide),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )

                RandomNumberCard(
                    iconRes = Res.drawable.ic_lotto645_random,
                    title = Res.string.lotto_645_random_title,
                    description = Res.string.lotto_645_random_description,
                    onClick = { viewModel.event(Event.OnRandomNumberGenerationClick(LottoType.LOTTO645)) },
                )
                RandomNumberCard(
                    iconRes = Res.drawable.ic_lotto720_random,
                    title = Res.string.lotto_720_random_title,
                    description = Res.string.lotto_720_random_description,
                    onClick = { viewModel.event(Event.OnRandomNumberGenerationClick(LottoType.LOTTO720)) },
                )
            }
        }
    }
}

/**
 * 복권 종류 하나.
 *
 * 예전에는 바깥에서 받은 `modifier`를 Card에 준 뒤 카드 안 Row에도 똑같이 한 번 더 줬다.
 * 그래서 클릭 핸들러와 접근성 노드가 두 개가 되고, 카드를 덮은 안쪽 Row가 탭을 가로채
 * 카드의 리플이 나오지 않았다. 클릭은 `Card(onClick=)`이 한 번만 받는다.
 */
@Composable
private fun RandomNumberCard(
    iconRes: DrawableResource,
    title: StringResource,
    description: StringResource,
    onClick: () -> Unit,
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = LottoShapeTokens.card,
        // 그림자와 테두리를 함께 주면 경계가 두 겹이 되어 탁해 보인다. 테두리 없이 톤으로만 띄운다.
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(LottoSpacing.base),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(LottoSpacing.base),
        ) {
            Image(
                modifier = Modifier.size(CardImageSize),
                painter = painterResource(iconRes),
                contentDescription = null,
            )

            // 글자가 남는 폭을 다 쓰게 둔다. 예전 SpaceAround는 글자 길이에 따라
            // 일러스트 위치가 좌우로 움직였다.
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(title),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Spacer(modifier = Modifier.height(LottoSpacing.xxs))
                Text(
                    text = stringResource(description),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
