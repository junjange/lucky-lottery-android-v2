import SwiftUI

@main
struct LuckyLotteryApp: App {
    @State private var router = AppRouter()

    init() {
        // TODO: Uncomment when Xcode + shared framework is ready
        // import LuckyLotteryShared
        // KoinHelperKt.initKoin()
    }

    var body: some Scene {
        WindowGroup {
            Group {
                if router.isLoggedIn {
                    NavigationStack(path: $router.path) {
                        MainScreen()
                            .navigationDestination(for: AppRoute.self) { route in
                                switch route {
                                case .main:
                                    MainScreen()
                                case .login:
                                    LoginScreen()
                                case .register(let idToken, let provider):
                                    RegisterScreen(idToken: idToken, provider: provider)
                                case .randomNumber:
                                    RandomNumberScreen()
                                case .randomNumberGeneration(let isLotto645):
                                    RandomNumberGenerationScreen(isLotto645: isLotto645)
                                case .notification(let lottoEnabled, let pensionEnabled):
                                    NotificationScreen(lottoEnabled: lottoEnabled, pensionEnabled: pensionEnabled)
                                case .editProfile(let nickname, let profilePath):
                                    EditProfileScreen(currentNickname: nickname, currentProfilePath: profilePath)
                                case .withdrawal(let provider):
                                    WithdrawalScreen(provider: provider)
                                }
                            }
                    }
                    .environment(router)
                } else {
                    SplashScreen()
                        .environment(router)
                }
            }
        }
    }
}
