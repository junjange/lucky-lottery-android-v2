import SwiftUI

struct SplashScreen: View {
    @Environment(AppRouter.self) private var router
    @State private var isAnimating = false

    var body: some View {
        VStack(spacing: 20) {
            Spacer()
            Image(systemName: "clover.fill")
                .resizable()
                .frame(width: 100, height: 100)
                .foregroundColor(.lottoGreen)
                .scaleEffect(isAnimating ? 1.0 : 0.5)
                .opacity(isAnimating ? 1.0 : 0.0)
                .animation(.easeOut(duration: 0.8), value: isAnimating)

            Text("일상속에서 행운을 찾다")
                .font(.title3)
                .foregroundColor(.lottoGray500)
                .opacity(isAnimating ? 1.0 : 0.0)
                .animation(.easeOut(duration: 0.8).delay(0.3), value: isAnimating)
            Spacer()
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity)
        .background(Color(.systemBackground))
        .onAppear {
            isAnimating = true
            DispatchQueue.main.asyncAfter(deadline: .now() + 2.0) {
                // TODO: Check JWT token and navigate accordingly
                router.isLoggedIn = true
            }
        }
    }
}
