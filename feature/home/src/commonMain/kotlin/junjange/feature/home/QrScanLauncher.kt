package junjange.feature.home

import androidx.compose.runtime.Composable

/**
 * QR 스캔 → (전면광고) → 스캔된 URL 열기 플로우의 런처.
 * 플랫폼이 지원하지 않으면 null을 반환한다.
 * Android는 Activity 기반 플로우(LottoNavHost → MainActivity)를 사용하므로 null.
 */
@Composable
expect fun rememberQrScanAndOpen(): (() -> Unit)?
