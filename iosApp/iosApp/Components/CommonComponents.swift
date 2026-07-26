import SwiftUI

// MARK: - Loading Dialog
struct LoadingDialog: View {
    var body: some View {
        ZStack {
            Color.black.opacity(0.3)
                .ignoresSafeArea()
            ProgressView()
                .scaleEffect(1.5)
                .tint(.white)
                .padding(30)
                .background(.ultraThinMaterial)
                .clipShape(RoundedRectangle(cornerRadius: 12))
        }
    }
}

// MARK: - Empty Screen
struct EmptyScreen: View {
    let message: String

    var body: some View {
        VStack(spacing: 12) {
            Image(systemName: "tray")
                .font(.system(size: 48))
                .foregroundColor(.lottoGray300)
            Text(message)
                .font(.body)
                .foregroundColor(.lottoGray500)
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity)
    }
}

// MARK: - Error Retry Screen
struct ErrorRetryScreen: View {
    let title: String
    let description: String
    let onRetry: () -> Void

    var body: some View {
        VStack(spacing: 5) {
            Spacer()
            Text(title)
                .font(.system(size: 18))
                .multilineTextAlignment(.center)

            Text(description)
                .font(.system(size: 16))
                .foregroundColor(.lottoGray500)
                .multilineTextAlignment(.center)

            Spacer().frame(height: 30)

            Button(action: onRetry) {
                Text("재시도")
                    .font(.system(size: 14, weight: .bold))
                    .foregroundColor(.white)
                    .frame(width: 120)
                    .padding(.vertical, 10)
                    .background(Color.lottoGreen)
                    .clipShape(RoundedRectangle(cornerRadius: 8))
            }
            Spacer()
        }
    }
}

// MARK: - Menu Button Bar
struct LottoButtonBar: View {
    let title: String
    var subtitle: String? = nil
    let action: () -> Void

    var body: some View {
        Button(action: action) {
            HStack {
                VStack(alignment: .leading, spacing: 4) {
                    Text(title)
                        .font(.system(size: 16))
                        .foregroundColor(.primary)
                    if let subtitle {
                        Text(subtitle)
                            .font(.system(size: 13))
                            .foregroundColor(.lottoGray500)
                    }
                }
                Spacer()
                Image(systemName: "chevron.right")
                    .font(.system(size: 14))
                    .foregroundColor(.lottoGray300)
            }
            .padding(.vertical, 14)
            .padding(.horizontal, 16)
        }
    }
}

// MARK: - Switch Bar
struct LottoSwitchBar: View {
    let title: String
    let description: String
    @Binding var isOn: Bool
    let onToggle: (Bool) -> Void

    var body: some View {
        VStack(alignment: .leading, spacing: 8) {
            Toggle(isOn: Binding(
                get: { isOn },
                set: { newValue in
                    isOn = newValue
                    onToggle(newValue)
                }
            )) {
                Text(title)
                    .font(.system(size: 16))
            }
            .tint(.lottoGreen)

            Text(description)
                .font(.system(size: 13))
                .foregroundColor(.lottoGray500)
        }
        .padding(.horizontal, 16)
        .padding(.vertical, 12)
    }
}

// MARK: - Two Button Dialog
struct LottoTwoButtonDialog: View {
    let title: String
    let message: String
    let confirmText: String
    let cancelText: String
    let onConfirm: () -> Void
    let onCancel: () -> Void

    var body: some View {
        VStack(spacing: 16) {
            Text(title)
                .font(.headline)
            Text(message)
                .font(.subheadline)
                .foregroundColor(.secondary)
                .multilineTextAlignment(.center)
            HStack(spacing: 12) {
                Button(action: onCancel) {
                    Text(cancelText)
                        .frame(maxWidth: .infinity)
                        .padding(.vertical, 12)
                        .background(Color.lottoGray100)
                        .clipShape(RoundedRectangle(cornerRadius: 8))
                }
                .foregroundColor(.primary)

                Button(action: onConfirm) {
                    Text(confirmText)
                        .frame(maxWidth: .infinity)
                        .padding(.vertical, 12)
                        .background(Color.lottoGreen)
                        .clipShape(RoundedRectangle(cornerRadius: 8))
                }
                .foregroundColor(.white)
            }
        }
        .padding(24)
        .background(.background)
        .clipShape(RoundedRectangle(cornerRadius: 16))
        .shadow(radius: 10)
        .padding(40)
    }
}
