import SwiftUI
import LuckyLotteryShared

/// SwiftUI 셸: TabView/NavigationStack(Liquid Glass) + .tint(브랜드그린).
/// 탭 콘텐츠는 commonMain Compose 화면을 ComposeUIViewController로 embed한다.
struct ContentView: View {
    private enum Tab: Hashable {
        case home, myNumber, randomNumber, setting
    }

    /// 탭 밖 전체화면 목적지. Android `LotteryNavHost`가 탭(`MAIN`) 밖에 둔 라우트와 같은 것들이다.
    private enum Route: Hashable {
        case randomGeneration(lottoType: String)
    }

    @SwiftUI.State private var showSplash = true
    @SwiftUI.State private var selectedTab: Tab = .home
    @SwiftUI.State private var myNumberPage: Int32 = 0
    @SwiftUI.State private var myNumberEpoch = 0
    /// 내 번호 화면이 탭 바를 요구하지 않는 상태. 지울 번호를 고르는 중이거나,
    /// 번호를 담는 전체 화면이 떠 있을 때. 이때는 탭 바를 내려 하단을 그 화면에 넘긴다.
    @SwiftUI.State private var myNumberChromeHidden = false
    @SwiftUI.State private var path: [Route] = []

    var body: some View {
        ZStack {
            // 스택을 TabView 밖에 둔다. 탭 안에 두고 `.toolbar(.hidden, for: .tabBar)`로 탭 바만
            // 가리면, 탭 바를 숨기고 되살리는 애니메이션이 push/pop과 맞물리지 않아
            // 돌아올 때 탭 바가 뒤늦게 따로 올라온다. 스택이 탭 바를 함께 밀어내면 감출 것이 없다.
            NavigationStack(path: $path) {
                tabShell
                    .toolbar(.hidden, for: .navigationBar)
                    .navigationDestination(for: Route.self) { route in
                        destination(for: route)
                    }
            }

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
        .tint(Color.brandPrimary)
    }

    private var tabShell: some View {
        TabView(selection: $selectedTab) {
            ComposeScreen { IosShellKt.homeViewController() }
                .ignoresSafeArea(.all)
                .tabItem { Label("홈", image: "ic_home") }
                .tag(Tab.home)

            ComposeScreen {
                IosShellKt.myNumberViewController(
                    initialPage: myNumberPage,
                    onChromeHidden: { hidden in
                        withAnimation(.easeInOut(duration: 0.25)) {
                            myNumberChromeHidden = hidden.boolValue
                        }
                    }
                )
            }
            .id(myNumberEpoch)
            .ignoresSafeArea(.all)
            .toolbar(myNumberChromeHidden ? .hidden : .visible, for: .tabBar)
            .tabItem {
                Label("내 번호", image: selectedTab == .myNumber ? "ic_clover" : "ic_clover_outlined")
            }
            .tag(Tab.myNumber)

            ComposeScreen {
                IosShellKt.randomNumberViewController(
                    navigateToGeneration: { lottoType in
                        path.append(.randomGeneration(lottoType: lottoType))
                    }
                )
            }
            .ignoresSafeArea(.all)
            .tabItem { Label("랜덤 번호", image: "ic_plus") }
            .tag(Tab.randomNumber)

            ComposeScreen { IosShellKt.settingViewController() }
            .ignoresSafeArea(.all)
            .tabItem { Label("설정", image: "ic_settings") }
            .tag(Tab.setting)
        }
    }

    /// 탭 밖 화면들. 오른쪽에서 밀려 들어오고 왼쪽 엣지 스와이프로 돌아온다.
    /// Compose 화면이 자기 상단 바를 그리므로 SwiftUI 내비게이션 바는 감춘다.
    @ViewBuilder
    private func destination(for route: Route) -> some View {
        switch route {
        case .randomGeneration(let lottoType):
            ComposeScreen {
                IosShellKt.randomNumberGenerationViewController(
                    lottoType: lottoType,
                    navigateToMyNumber: { page in
                        // 저장 후에는 스택을 비우고 내 번호 탭으로 보낸다.
                        path.removeAll()
                        myNumberPage = Int32(page) ?? 0
                        myNumberEpoch += 1
                        selectedTab = .myNumber
                    },
                    onBack: { popRoute() }
                )
            }
            .ignoresSafeArea(.all)
            .toolbar(.hidden, for: .navigationBar)
        }
    }

    /// 엣지 스와이프로 이미 빠져나온 뒤에 콜백이 한 번 더 오면 빈 스택에서 지우게 되므로 확인한다.
    private func popRoute() {
        guard !path.isEmpty else { return }
        path.removeLast()
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
