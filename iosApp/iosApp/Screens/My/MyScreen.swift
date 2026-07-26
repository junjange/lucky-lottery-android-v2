import SwiftUI

struct MyScreen: View {
    @Environment(AppRouter.self) private var router
    @State private var nickname = "사용자"
    @State private var profilePath: String?
    @State private var provider = "KAKAO"
    @State private var lottoNotification = false
    @State private var pensionNotification = false
    @State private var showLogoutAlert = false

    var body: some View {
        ScrollView {
            VStack(spacing: 0) {
                // Profile Card
                profileCard
                    .padding(16)

                // Menu Items
                VStack(spacing: 0) {
                    LottoButtonBar(title: "앱 알림") {
                        router.navigate(to: .notification(
                            lottoEnabled: lottoNotification,
                            pensionEnabled: pensionNotification
                        ))
                    }
                    Divider().padding(.horizontal, 16)

                    LottoButtonBar(title: "버전 정보", subtitle: "3.1.1") {}
                    Divider().padding(.horizontal, 16)

                    LottoButtonBar(title: "이용약관") {
                        // TODO: Open usage terms URL
                    }
                    Divider().padding(.horizontal, 16)

                    LottoButtonBar(title: "로그아웃") {
                        showLogoutAlert = true
                    }
                    Divider().padding(.horizontal, 16)

                    LottoButtonBar(title: "회원 탈퇴") {
                        router.navigate(to: .withdrawal(provider: provider))
                    }
                }
                .background(Color(.secondarySystemGroupedBackground))
                .clipShape(RoundedRectangle(cornerRadius: 12))
                .padding(.horizontal, 16)
            }
        }
        .alert("로그아웃", isPresented: $showLogoutAlert) {
            Button("로그아웃", role: .destructive) { logout() }
            Button("취소", role: .cancel) {}
        } message: {
            Text("정말 로그아웃하시겠습니까?")
        }
        .onAppear { loadUserInfo() }
    }

    private var profileCard: some View {
        HStack(spacing: 16) {
            ZStack(alignment: .bottomTrailing) {
                Circle()
                    .fill(Color.lottoGray100)
                    .frame(width: 64, height: 64)
                    .overlay {
                        Image(systemName: "person.fill")
                            .font(.system(size: 28))
                            .foregroundColor(.lottoGray300)
                    }
                Button(action: {
                    router.navigate(to: .editProfile(nickname: nickname, profilePath: profilePath))
                }) {
                    Image(systemName: "camera.fill")
                        .font(.system(size: 10))
                        .foregroundColor(.white)
                        .padding(6)
                        .background(Color.lottoGreen)
                        .clipShape(Circle())
                }
            }

            VStack(alignment: .leading, spacing: 4) {
                Text(nickname)
                    .font(.system(size: 18, weight: .bold))
                Text(provider == "KAKAO" ? "카카오 로그인" : "Google 로그인")
                    .font(.system(size: 13))
                    .foregroundColor(.lottoGray500)
            }
            Spacer()
        }
        .padding(16)
        .background(Color.lottoDarkSurface)
        .clipShape(RoundedRectangle(cornerRadius: 12))
    }

    private func loadUserInfo() {
        // TODO: Call GetUserMyInfoUseCase via shared module
    }

    private func logout() {
        // TODO: Call PostLogoutUseCase via shared module
        router.isLoggedIn = false
        router.popToRoot()
    }
}
