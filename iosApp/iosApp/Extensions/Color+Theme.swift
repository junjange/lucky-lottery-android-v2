import SwiftUI

extension Color {
    static let lottoGreen = Color(red: 0.0, green: 0.72, blue: 0.42)
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
