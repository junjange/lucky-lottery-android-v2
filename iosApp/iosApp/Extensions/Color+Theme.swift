import SwiftUI

extension Color {
    /// 브랜드 시드 컬러 #30AA5B — Compose 스킴의 BrandPrimary, Android XML의 brand_primary와 같은 값.
    /// SwiftUI 셸(TabView/NavigationStack)의 tint로만 쓰이고, 화면 내부 색은 Compose 테마가 담당한다.
    static let brandPrimary = Color(red: 0x30 / 255.0, green: 0xAA / 255.0, blue: 0x5B / 255.0)
}
