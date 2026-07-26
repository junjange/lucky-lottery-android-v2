import SwiftUI

struct LottoBall: View {
    let number: Int
    var size: CGFloat = 36

    var body: some View {
        ZStack {
            Circle()
                .fill(Color.lottoBallColor(for: number))
                .frame(width: size, height: size)
            Text("\(number)")
                .font(.system(size: size * 0.4, weight: .bold))
                .foregroundColor(.white)
        }
    }
}

struct PensionLottoBall: View {
    let number: Int
    var size: CGFloat = 36

    var body: some View {
        ZStack {
            Circle()
                .fill(Color.lottoGreen)
                .frame(width: size, height: size)
            Text("\(number)")
                .font(.system(size: size * 0.4, weight: .bold))
                .foregroundColor(.white)
        }
    }
}

#Preview {
    HStack {
        LottoBall(number: 7)
        LottoBall(number: 15)
        LottoBall(number: 25)
        LottoBall(number: 35)
        LottoBall(number: 42)
    }
}
