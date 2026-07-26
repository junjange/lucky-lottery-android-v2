import SwiftUI

struct MainScreen: View {
    @Environment(AppRouter.self) private var router
    @State private var selectedTab: MainTab = .home

    var body: some View {
        TabView(selection: $selectedTab) {
            HomeScreen()
                .tabItem {
                    Label(MainTab.home.title, systemImage: MainTab.home.icon)
                }
                .tag(MainTab.home)

            MyNumberScreen()
                .tabItem {
                    Label(MainTab.myNumber.title, systemImage: MainTab.myNumber.icon)
                }
                .tag(MainTab.myNumber)

            RandomNumberScreen()
                .tabItem {
                    Label(MainTab.randomNumber.title, systemImage: MainTab.randomNumber.icon)
                }
                .tag(MainTab.randomNumber)

            SettingScreen()
                .tabItem {
                    Label(MainTab.setting.title, systemImage: MainTab.setting.icon)
                }
                .tag(MainTab.setting)
        }
        .tint(.lottoGreen)
    }
}
