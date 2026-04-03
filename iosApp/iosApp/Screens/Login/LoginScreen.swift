import SwiftUI

struct LoginScreen: View {
    @Environment(AppRouter.self) private var router
    @State private var isLoading = false

    var body: some View {
        VStack {
            Spacer()

            Image(systemName: "clover.fill")
                .resizable()
                .frame(width: 100, height: 100)
                .foregroundColor(.lottoGreen)

            Text("행운 복권")
                .font(.title)
                .fontWeight(.bold)
                .padding(.top, 16)

            Spacer()

            VStack(spacing: 12) {
                // Apple Sign In
                Button(action: { signInWithApple() }) {
                    HStack {
                        Image(systemName: "apple.logo")
                        Text("Apple로 로그인")
                    }
                    .frame(maxWidth: .infinity)
                    .padding(.vertical, 14)
                    .background(Color.primary)
                    .foregroundColor(Color(.systemBackground))
                    .clipShape(RoundedRectangle(cornerRadius: 12))
                }

                // Google Sign In (placeholder)
                Button(action: { signInWithGoogle() }) {
                    HStack {
                        Image(systemName: "g.circle.fill")
                        Text("Google로 로그인")
                    }
                    .frame(maxWidth: .infinity)
                    .padding(.vertical, 14)
                    .background(Color.white)
                    .foregroundColor(.black)
                    .overlay(
                        RoundedRectangle(cornerRadius: 12)
                            .stroke(Color.lottoGray300, lineWidth: 1)
                    )
                    .clipShape(RoundedRectangle(cornerRadius: 12))
                }
            }
            .padding(.horizontal, 24)
            .padding(.bottom, 60)
        }
        .overlay {
            if isLoading { LoadingDialog() }
        }
    }

    private func signInWithApple() {
        // TODO: Implement Apple Sign In with AuthenticationServices
        router.isLoggedIn = true
    }

    private func signInWithGoogle() {
        // TODO: Implement Google Sign In with Firebase Auth iOS SDK
        router.isLoggedIn = true
    }
}
