package junjange.feature.notification

import androidx.compose.runtime.Composable

/**
 * NotificationScreen의 플랫폼 래퍼 — 알림 권한 요청/설정 이동을 플랫폼별로 처리한다.
 * Android: POST_NOTIFICATIONS 런타임 권한, iOS: UNUserNotificationCenter 인가.
 */
@Composable
expect fun NotificationRoute(
    viewModel: NotificationViewModel,
    finish: () -> Unit,
)
