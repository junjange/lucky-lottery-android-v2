import Foundation
import GoogleMobileAds
import LuckyLotteryShared
import UIKit

/// AdMob 광고 단위 설정. 릴리스 빌드 전 AdMob 콘솔에서 발급한 iOS 광고 단위 ID로 교체할 것.
enum AdConfig {
    static let bannerAdUnitID = ""
    static let interstitialAdUnitID = ""

    static var resolvedBannerAdUnitID: String {
        #if DEBUG
        return "ca-app-pub-3940256099942544/2934735716" // Google 공식 iOS 테스트 배너
        #else
        return bannerAdUnitID
        #endif
    }

    static var resolvedInterstitialAdUnitID: String {
        #if DEBUG
        return "ca-app-pub-3940256099942544/4411468910" // Google 공식 iOS 테스트 전면광고
        #else
        return interstitialAdUnitID
        #endif
    }
}

/// Kotlin(core/ui iosMain)의 IosAdBridge에 실제 광고 구현을 주입한다.
enum AdBridgeSetup {
    static func register() {
        MobileAds.shared.start(completionHandler: nil)

        IosAdBridge.shared.bannerFactory = {
            let banner = BannerView(adSize: AdSizeBanner)
            banner.backgroundColor = .clear
            banner.adUnitID = AdConfig.resolvedBannerAdUnitID
            banner.rootViewController = rootViewController()
            banner.load(Request())
            return banner
        }

        InterstitialAdManager.shared.preload()
        IosAdBridge.shared.showInterstitial = { onDismissed in
            InterstitialAdManager.shared.show(onDismissed: {
                onDismissed()
            })
        }
    }

    static func rootViewController() -> UIViewController? {
        var top = UIApplication.shared.connectedScenes
            .compactMap { $0 as? UIWindowScene }
            .flatMap { $0.windows }
            .first { $0.isKeyWindow }?
            .rootViewController
        while let presented = top?.presentedViewController {
            top = presented
        }
        return top
    }
}

/// 전면광고 로드/표시 관리. 광고가 닫히면 onDismissed를 호출하고 다음 광고를 미리 로드한다.
final class InterstitialAdManager: NSObject, FullScreenContentDelegate {
    static let shared = InterstitialAdManager()

    private var interstitial: InterstitialAd?
    private var onDismissed: (() -> Void)?

    func preload() {
        InterstitialAd.load(
            with: AdConfig.resolvedInterstitialAdUnitID,
            request: Request()
        ) { [weak self] ad, _ in
            ad?.fullScreenContentDelegate = self
            self?.interstitial = ad
        }
    }

    func show(onDismissed: @escaping () -> Void) {
        guard let ad = interstitial, let root = AdBridgeSetup.rootViewController() else {
            onDismissed()
            preload()
            return
        }
        self.onDismissed = onDismissed
        ad.present(from: root)
    }

    func adDidDismissFullScreenContent(_ ad: FullScreenPresentingAd) {
        interstitial = nil
        let callback = onDismissed
        onDismissed = nil
        preload()
        callback?()
    }

    func ad(_ ad: FullScreenPresentingAd, didFailToPresentFullScreenContentWithError error: Error) {
        interstitial = nil
        let callback = onDismissed
        onDismissed = nil
        preload()
        callback?()
    }
}
