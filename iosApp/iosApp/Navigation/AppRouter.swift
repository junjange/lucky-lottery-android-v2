import SwiftUI

enum AppRoute: Hashable {
    case main
    case login
    case register(idToken: String, provider: String)
    case randomNumber
    case randomNumberGeneration(isLotto645: Bool)
    case notification(lottoEnabled: Bool, pensionEnabled: Bool)
    case editProfile(nickname: String, profilePath: String?)
    case withdrawal(provider: String)
}

enum MainTab: Int, CaseIterable {
    case home = 0
    case myNumber = 1
    case randomNumber = 2
    case setting = 3

    var title: String {
        switch self {
        case .home: return "홈"
        case .myNumber: return "내 번호"
        case .randomNumber: return "랜덤 번호"
        case .setting: return "설정"
        }
    }

    var icon: String {
        switch self {
        case .home: return "house.fill"
        case .myNumber: return "number.circle.fill"
        case .randomNumber: return "dice.fill"
        case .setting: return "gearshape.fill"
        }
    }
}

@Observable
class AppRouter {
    var path = NavigationPath()
    var isLoggedIn = false

    func navigate(to route: AppRoute) {
        path.append(route)
    }

    func pop() {
        if !path.isEmpty {
            path.removeLast()
        }
    }

    func popToRoot() {
        path = NavigationPath()
    }
}
