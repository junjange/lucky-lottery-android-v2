import SwiftUI

struct WithdrawalScreen: View {
    @Environment(AppRouter.self) private var router
    let provider: String

    @State private var step = 1
    @State private var showConfirmDialog = false

    var body: some View {
        VStack {
            if step == 1 {
                step1View
            } else {
                step2View
            }
        }
        .navigationTitle("회원 탈퇴")
        .navigationBarTitleDisplayMode(.inline)
        .alert("회원 탈퇴", isPresented: $showConfirmDialog) {
            Button("탈퇴", role: .destructive) { withdraw() }
            Button("취소", role: .cancel) {}
        } message: {
            Text("정말 탈퇴하시겠습니까?\n모든 데이터가 삭제됩니다.")
        }
    }

    private var step1View: some View {
        VStack(spacing: 24) {
            Spacer()

            Image(systemName: "exclamationmark.triangle.fill")
                .font(.system(size: 48))
                .foregroundColor(.orange)

            Text("정말 떠나시겠어요?")
                .font(.title2.bold())

            Text("탈퇴하시면 저장된 번호와\n모든 데이터가 삭제됩니다.")
                .font(.body)
                .foregroundColor(.lottoGray500)
                .multilineTextAlignment(.center)

            Spacer()

            VStack(spacing: 12) {
                Button(action: { showConfirmDialog = true }) {
                    Text("탈퇴하기")
                        .font(.system(size: 16, weight: .bold))
                        .foregroundColor(.white)
                        .frame(maxWidth: .infinity)
                        .padding(.vertical, 14)
                        .background(Color.red)
                        .clipShape(RoundedRectangle(cornerRadius: 12))
                }

                Button(action: { router.pop() }) {
                    Text("취소")
                        .font(.system(size: 16))
                        .foregroundColor(.lottoGray500)
                        .frame(maxWidth: .infinity)
                        .padding(.vertical, 14)
                }
            }
            .padding(.horizontal, 24)
            .padding(.bottom, 24)
        }
    }

    private var step2View: some View {
        VStack(spacing: 24) {
            Spacer()

            Image(systemName: "hand.wave.fill")
                .font(.system(size: 48))
                .foregroundColor(.lottoGreen)

            Text("이용해주셔서 감사합니다")
                .font(.title2.bold())

            Text("더 나은 서비스로 다시 찾아뵙겠습니다.")
                .font(.body)
                .foregroundColor(.lottoGray500)

            Spacer()

            Button(action: {
                router.isLoggedIn = false
                router.popToRoot()
            }) {
                Text("타이틀로 돌아가기")
                    .font(.system(size: 16, weight: .bold))
                    .foregroundColor(.white)
                    .frame(maxWidth: .infinity)
                    .padding(.vertical, 14)
                    .background(Color.lottoGreen)
                    .clipShape(RoundedRectangle(cornerRadius: 12))
            }
            .padding(.horizontal, 24)
            .padding(.bottom, 24)
        }
    }

    private func withdraw() {
        // TODO: Call DeleteMeUseCase + DeleteLocalDataUseCase via shared module
        step = 2
    }
}
