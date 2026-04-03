import SwiftUI

struct NotificationScreen: View {
    @State var lottoEnabled: Bool
    @State var pensionEnabled: Bool

    var body: some View {
        VStack(spacing: 0) {
            LottoSwitchBar(
                title: "로또 6/45 알림",
                description: "매주 토요일 당첨번호 발표 알림을 받습니다",
                isOn: $lottoEnabled,
                onToggle: { enabled in
                    // TODO: Call PatchLotteryNotificationUseCase via shared module
                }
            )

            Divider().padding(.horizontal, 16)

            LottoSwitchBar(
                title: "연금복권 720+ 알림",
                description: "매주 목요일 당첨번호 발표 알림을 받습니다",
                isOn: $pensionEnabled,
                onToggle: { enabled in
                    // TODO: Call PatchPensionLotteryNotificationUseCase via shared module
                }
            )

            Spacer()
        }
        .navigationTitle("알림 설정")
        .navigationBarTitleDisplayMode(.inline)
    }
}
