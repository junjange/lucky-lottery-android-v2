import SwiftUI

struct SettingScreen: View {
    @Environment(AppRouter.self) private var router
    @State private var lottoNotification = false
    @State private var pensionNotification = false

    var body: some View {
        VStack(spacing: 0) {
            LottoButtonBar(title: "앱 알림") {
                router.navigate(to: .notification(
                    lottoEnabled: lottoNotification,
                    pensionEnabled: pensionNotification
                ))
            }
            Divider().padding(.horizontal, 16)

            LottoButtonBar(title: "이용약관") {
                // TODO: Open usage terms URL
            }
            Divider().padding(.horizontal, 16)

            LottoButtonBar(title: "버전 정보", subtitle: "1.0.0") {}
            Divider().padding(.horizontal, 16)

            LottoButtonBar(title: "앱 리뷰 남기기") {
                // TODO: Open App Store review
            }

            Spacer()
        }
    }
}
