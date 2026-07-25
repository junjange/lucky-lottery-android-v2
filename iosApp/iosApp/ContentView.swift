import SwiftUI
import LuckyLotteryShared

/// 브랜드 시드 컬러 (#30AA5B) — 셸의 tint로만 주입하고 나머지는 OS 기본을 따른다.
private let brandPrimary = Color(red: 0x30 / 255.0, green: 0xAA / 255.0, blue: 0x5B / 255.0)

/// SwiftUI 셸: TabView/NavigationStack(Liquid Glass) + .tint(브랜드그린).
/// 탭 콘텐츠는 commonMain Compose 화면을 ComposeUIViewController로 embed한다.
struct ContentView: View {
    private enum Tab: Hashable {
        case home, myNumber, randomNumber, setting
    }

    @SwiftUI.State private var showSplash = true
    @SwiftUI.State private var selectedTab: Tab = .home
    @SwiftUI.State private var myNumberPage: Int32 = 0
    @SwiftUI.State private var myNumberEpoch = 0

    var body: some View {
        ZStack {
            tabShell

            if showSplash {
                ComposeScreen {
                    IosShellKt.splashViewController {
                        withAnimation(.easeOut(duration: 0.3)) {
                            showSplash = false
                        }
                    }
                }
                .ignoresSafeArea(.all)
                .transition(.opacity)
            }
        }
        .tint(brandPrimary)
    }

    private var tabShell: some View {
        TabView(selection: $selectedTab) {
            ComposeScreen { IosShellKt.homeViewController() }
                .ignoresSafeArea(.all)
                .tabItem { Label("홈", systemImage: "house.fill") }
                .tag(Tab.home)

            ComposeScreen { IosShellKt.myNumberViewController(initialPage: myNumberPage) }
                .id(myNumberEpoch)
                .ignoresSafeArea(.all)
                .tabItem { Label("내 번호", systemImage: "ticket.fill") }
                .tag(Tab.myNumber)

            RandomNumberTab(
                onFinished: { selectedTab = .home },
                onSaved: { page in
                    myNumberPage = Int32(page) ?? 0
                    myNumberEpoch += 1
                    selectedTab = .myNumber
                }
            )
            .tabItem { Label("랜덤 번호", systemImage: "dice.fill") }
            .tag(Tab.randomNumber)

            SettingTab()
                .tabItem { Label("설정", systemImage: "gearshape.fill") }
                .tag(Tab.setting)
        }
    }
}

/// 랜덤 번호 탭: 목록(루트) → 번호 생성(푸시). 화면 자체 상단바를 쓰므로 시스템 내비바는 숨긴다.
private struct RandomNumberTab: View {
    let onFinished: () -> Void
    let onSaved: (String) -> Void

    @SwiftUI.State private var generationLottoType: String?

    var body: some View {
        NavigationStack {
            ComposeScreen {
                IosShellKt.randomNumberViewController(
                    navigateToGeneration: { lottoType in generationLottoType = lottoType },
                    onBack: onFinished
                )
            }
            .ignoresSafeArea(.all)
            .toolbar(.hidden, for: .navigationBar)
            .navigationDestination(isPresented: isGenerationPresented) {
                ComposeScreen {
                    IosShellKt.randomNumberGenerationViewController(
                        lottoType: generationLottoType ?? "",
                        navigateToMyNumber: { page in
                            generationLottoType = nil
                            onSaved(page)
                        },
                        onBack: { generationLottoType = nil }
                    )
                }
                .ignoresSafeArea(.all)
                .toolbar(.hidden, for: .navigationBar)
                .toolbar(.hidden, for: .tabBar)
            }
        }
    }

    private var isGenerationPresented: Binding<Bool> {
        Binding(
            get: { generationLottoType != nil },
            set: { if !$0 { generationLottoType = nil } }
        )
    }
}

/// 설정 탭: 설정(루트) → 알림 설정(푸시).
private struct SettingTab: View {
    @SwiftUI.State private var showNotification = false

    var body: some View {
        NavigationStack {
            ComposeScreen {
                IosShellKt.settingViewController(
                    navigateToNotification: { showNotification = true }
                )
            }
            .ignoresSafeArea(.all)
            .toolbar(.hidden, for: .navigationBar)
            .navigationDestination(isPresented: $showNotification) {
                ComposeScreen {
                    IosShellKt.notificationViewController(onBack: { showNotification = false })
                }
                .ignoresSafeArea(.all)
                .toolbar(.hidden, for: .navigationBar)
                .toolbar(.hidden, for: .tabBar)
            }
        }
    }
}

/// Compose 화면 embed용 래퍼.
struct ComposeScreen: UIViewControllerRepresentable {
    let factory: () -> UIViewController

    init(factory: @escaping () -> UIViewController) {
        self.factory = factory
    }

    func makeUIViewController(context: Context) -> UIViewController {
        factory()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

#Preview {
    ContentView()
}
