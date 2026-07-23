package junjange.core.ui.platform

import platform.UIKit.UIView

/**
 * Swift에서 구현을 주입하는 광고 브리지.
 * Kotlin/Native는 Google Mobile Ads SDK(iOS)를 직접 참조할 수 없으므로,
 * 앱 시작 시 Swift가 팩토리를 등록한다.
 *
 * ```swift
 * IosAdBridge.shared.bannerFactory = { ... }
 * IosAdBridge.shared.showInterstitial = { onDismissed in ... }
 * ```
 */
object IosAdBridge {
    /** 320x50 배너 UIView를 생성해 반환. 미등록 시 배너 영역은 비워진다. */
    var bannerFactory: (() -> UIView)? = null

    /**
     * 전면광고를 표시하고, 광고가 닫히면(또는 표시 실패 시) onDismissed를 호출한다.
     * 미등록 시 호출부는 광고 없이 다음 동작을 이어간다.
     */
    var showInterstitial: ((onDismissed: () -> Unit) -> Unit)? = null
}

/**
 * Swift에서 구현을 주입하는 QR 스캐너 브리지.
 * scan 호출 시 카메라 스캐너를 모달로 띄우고, 인식 결과(취소 시 null)를 콜백으로 돌려준다.
 */
object IosQrScannerBridge {
    var scan: ((onResult: (String?) -> Unit) -> Unit)? = null
}

/**
 * Swift에서 구현을 주입하는 이미지 피커 브리지.
 * pickImage 호출 시 PHPicker를 모달로 띄우고, 선택한 이미지의 임시 파일 경로(취소 시 null)를 콜백으로 돌려준다.
 */
object IosImagePickerBridge {
    var pickImage: ((onResult: (String?) -> Unit) -> Unit)? = null
}
