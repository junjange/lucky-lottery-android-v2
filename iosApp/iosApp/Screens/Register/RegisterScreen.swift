import SwiftUI

struct RegisterScreen: View {
    @Environment(AppRouter.self) private var router
    let idToken: String
    let provider: String

    @State private var nickname = ""
    @State private var profileImage: UIImage?
    @State private var showImagePicker = false

    var body: some View {
        VStack(spacing: 24) {
            // Profile Image
            Button(action: { showImagePicker = true }) {
                ZStack(alignment: .bottomTrailing) {
                    if let image = profileImage {
                        Image(uiImage: image)
                            .resizable()
                            .scaledToFill()
                            .frame(width: 100, height: 100)
                            .clipShape(Circle())
                    } else {
                        Circle()
                            .fill(Color.lottoGray100)
                            .frame(width: 100, height: 100)
                            .overlay {
                                Image(systemName: "person.fill")
                                    .font(.system(size: 40))
                                    .foregroundColor(.lottoGray300)
                            }
                    }
                    Image(systemName: "camera.fill")
                        .font(.system(size: 14))
                        .foregroundColor(.white)
                        .padding(8)
                        .background(Color.lottoGreen)
                        .clipShape(Circle())
                }
            }
            .padding(.top, 40)

            // Nickname Field
            VStack(alignment: .leading, spacing: 8) {
                Text("닉네임")
                    .font(.system(size: 14))
                    .foregroundColor(.lottoGray500)
                HStack {
                    TextField("닉네임을 입력해주세요", text: $nickname)
                        .textFieldStyle(.plain)
                    if !nickname.isEmpty {
                        Button(action: { nickname = "" }) {
                            Image(systemName: "xmark.circle.fill")
                                .foregroundColor(.lottoGray300)
                        }
                    }
                }
                .padding(12)
                .background(Color.lottoGray100)
                .clipShape(RoundedRectangle(cornerRadius: 8))
            }
            .padding(.horizontal, 24)

            Spacer()

            // Done Button
            Button(action: register) {
                Text("완료")
                    .font(.system(size: 16, weight: .bold))
                    .foregroundColor(.white)
                    .frame(maxWidth: .infinity)
                    .padding(.vertical, 14)
                    .background(nickname.isEmpty ? Color.lottoGray300 : Color.lottoGreen)
                    .clipShape(RoundedRectangle(cornerRadius: 12))
            }
            .disabled(nickname.isEmpty)
            .padding(.horizontal, 24)
            .padding(.bottom, 24)
        }
        .navigationTitle("프로필 설정")
        .navigationBarTitleDisplayMode(.inline)
    }

    private func register() {
        // TODO: Call PostRegisterUseCase via shared module
        router.isLoggedIn = true
        router.popToRoot()
    }
}
