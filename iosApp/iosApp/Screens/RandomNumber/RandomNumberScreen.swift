import SwiftUI

struct RandomNumberScreen: View {
    @Environment(AppRouter.self) private var router

    var body: some View {
        VStack(spacing: 20) {
            lottoCard(
                title: "로또 6/45",
                description: "1~45 중 6개 번호를 랜덤 생성",
                icon: "dice.fill",
                action: { router.navigate(to: .randomNumberGeneration(isLotto645: true)) }
            )

            lottoCard(
                title: "연금복권 720+",
                description: "각 자리 0~9 랜덤 생성",
                icon: "dice.fill",
                action: { router.navigate(to: .randomNumberGeneration(isLotto645: false)) }
            )

            Spacer()
        }
        .padding(16)
        .navigationTitle("랜덤 번호 생성")
        .navigationBarTitleDisplayMode(.inline)
    }

    private func lottoCard(title: String, description: String, icon: String, action: @escaping () -> Void) -> some View {
        Button(action: action) {
            HStack(spacing: 16) {
                Image(systemName: icon)
                    .font(.system(size: 32))
                    .foregroundColor(.lottoGreen)

                VStack(alignment: .leading, spacing: 4) {
                    Text(title)
                        .font(.system(size: 18, weight: .bold))
                        .foregroundColor(.primary)
                    Text(description)
                        .font(.system(size: 14))
                        .foregroundColor(.lottoGray500)
                }

                Spacer()

                Image(systemName: "chevron.right")
                    .foregroundColor(.lottoGray300)
            }
            .padding(20)
            .background(Color(.secondarySystemGroupedBackground))
            .clipShape(RoundedRectangle(cornerRadius: 16))
        }
    }
}
