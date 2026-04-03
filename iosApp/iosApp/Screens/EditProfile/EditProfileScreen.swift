import SwiftUI

struct EditProfileScreen: View {
    @Environment(\.dismiss) private var dismiss
    let currentNickname: String
    let currentProfilePath: String?

    @State private var nickname: String
    @State private var profileImage: UIImage?
    @State private var showImagePicker = false
    @State private var isLoading = false

    init(currentNickname: String, currentProfilePath: String?) {
        self.currentNickname = currentNickname
        self.currentProfilePath = currentProfilePath
        _nickname = State(initialValue: currentNickname)
    }

    private var hasChanges: Bool {
        nickname != currentNickname || profileImage != nil
    }

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
            .padding(.top, 32)

            // Nickname
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

            Button(action: updateProfile) {
                Text("완료")
                    .font(.system(size: 16, weight: .bold))
                    .foregroundColor(.white)
                    .frame(maxWidth: .infinity)
                    .padding(.vertical, 14)
                    .background(hasChanges && !nickname.isEmpty ? Color.lottoGreen : Color.lottoGray300)
                    .clipShape(RoundedRectangle(cornerRadius: 12))
            }
            .disabled(!hasChanges || nickname.isEmpty)
            .padding(.horizontal, 24)
            .padding(.bottom, 24)
        }
        .navigationTitle("프로필 수정")
        .navigationBarTitleDisplayMode(.inline)
        .overlay { if isLoading { LoadingDialog() } }
    }

    private func updateProfile() {
        isLoading = true
        // TODO: Call ImagesUploadUseCase + PatchUserProfileUseCase via shared module
        DispatchQueue.main.asyncAfter(deadline: .now() + 1) {
            isLoading = false
            dismiss()
        }
    }
}
