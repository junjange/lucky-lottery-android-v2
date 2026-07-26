import Foundation
import GoogleMobileAds
import LuckyLotteryShared
import UIKit

/// AdMob 광고 단위 설정. 실제 값은 Configuration/{Debug,Release}.xcconfig에 있고,
/// Info.plist를 거쳐 번들에 주입된다(Debug는 Google 테스트 ID, Release는 iOS 전용 ID).
enum AdConfig {
    static let bannerAdUnitID = infoPlistString("BannerAdUnitID")
    static let interstitialAdUnitID = infoPlistString("InterstitialAdUnitID")

    private static func infoPlistString(_ key: String) -> String {
        Bundle.main.object(forInfoDictionaryKey: key) as? String ?? ""
    }
}

/// Kotlin(core/ui iosMain)의 IosAdBridge에 실제 광고 구현을 주입한다.
enum AdBridgeSetup {
    static func register() {
        MobileAds.shared.start(completionHandler: nil)

        IosAdBridge.shared.bannerFactory = {
            let banner = BannerView(adSize: AdSizeBanner)
            banner.backgroundColor = .clear
            banner.adUnitID = AdConfig.bannerAdUnitID
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
            with: AdConfig.interstitialAdUnitID,
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
