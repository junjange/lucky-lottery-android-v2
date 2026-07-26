import SwiftUI

extension Color {
    /// 브랜드 시드 컬러 #30AA5B — Compose 스킴의 BrandPrimary, Android XML의 brand_primary와 같은 값.
    static let brandPrimary = Color(red: 0x30 / 255.0, green: 0xAA / 255.0, blue: 0x5B / 255.0)

    /// 기존 이름 유지용 별칭. 별도의 초록을 쓰지 않고 브랜드 시드 하나로 수렴시킨다.
    static let lottoGreen = brandPrimary
    static let lottoBackground = Color(UIColor.systemGroupedBackground)
    static let lottoDarkSurface = Color(red: 0.15, green: 0.15, blue: 0.15)
    static let lottoGray100 = Color(red: 0.96, green: 0.96, blue: 0.96)
    static let lottoGray300 = Color(red: 0.78, green: 0.78, blue: 0.78)
    static let lottoGray500 = Color(red: 0.55, green: 0.55, blue: 0.55)

    static func lottoBallColor(for number: Int) -> Color {
        switch number {
        case 1...10: return .yellow
        case 11...20: return .blue
        case 21...30: return .red
        case 31...40: return .gray
        case 41...45: return .green
        default: return .gray
        }
    }
}
