package junjange.core.designsystem.theme

import androidx.compose.ui.unit.dp

/**
 * 간격 토큰. 4dp 배수만 쓴다.
 *
 * 화면이 올드해 보이는 가장 큰 원인이 5dp/15dp 같은 임의값이 섞여 리듬이 깨지는 것이므로,
 * 여백은 항상 이 스케일에서 고른다. 이름은 용도가 아니라 크기 순서로 두어 어디서나 재사용한다.
 */
object LottoSpacing {
    /** 2dp — 붙어 있어야 하는 글자 사이 */
    val xxs = 2.dp

    /** 4dp — 라벨과 값 */
    val xs = 4.dp

    /** 8dp — 같은 그룹 안의 요소 */
    val sm = 8.dp

    /** 12dp — 카드 내부 행 사이 */
    val md = 12.dp

    /** 16dp — 카드 내부 패딩 기본값 */
    val base = 16.dp

    /** 20dp — 화면 좌우 여백 */
    val lg = 20.dp

    /** 24dp — 카드 사이 */
    val xl = 24.dp

    /** 32dp — 섹션 사이 */
    val xxl = 32.dp

    /** 40dp — 화면 상단 제목 아래 */
    val xxxl = 40.dp

    /** 화면 좌우 공통 여백. 모든 화면이 같은 값을 써야 스크롤 시 정렬이 흔들리지 않는다. */
    val screenHorizontal = lg

    /** 탭한 영역이 손가락보다 작지 않도록 하는 최소 크기 */
    val minTouchTarget = 48.dp
}
