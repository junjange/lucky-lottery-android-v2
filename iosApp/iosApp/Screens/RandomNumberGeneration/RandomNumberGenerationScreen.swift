import SwiftUI

struct RandomNumberGenerationScreen: View {
    @Environment(AppRouter.self) private var router
    let isLotto645: Bool

    @State private var numbers: [Int] = []
    @State private var group: Int?
    @State private var round = 0
    @State private var winningDate = ""
    @State private var isGenerating = false
    @State private var saveEnabled = false
    @State private var showSavedMessage = false

    var body: some View {
        VStack(spacing: 24) {
            // Round Info
            VStack(spacing: 4) {
                Text("\(round)회")
                    .font(.system(size: 20, weight: .bold))
                Text(winningDate)
                    .font(.system(size: 13))
                    .foregroundColor(.lottoGray500)
            }
            .padding(.top, 20)

            // Numbers Display
            VStack(spacing: 12) {
                Text(isLotto645 ? "로또 6/45" : "연금복권 720+")
                    .font(.system(size: 14))
                    .foregroundColor(.lottoGray500)

                HStack(spacing: 8) {
                    if isLotto645 {
                        ForEach(0..<6, id: \.self) { index in
                            if index < numbers.count {
                                LottoBall(number: numbers[index], size: 46)
                                    .transition(.scale.combined(with: .opacity))
                            } else {
                                Circle()
                                    .fill(Color.lottoGray100)
                                    .frame(width: 46, height: 46)
                                    .overlay {
                                        Text("?")
                                            .font(.system(size: 18, weight: .bold))
                                            .foregroundColor(.lottoGray300)
                                    }
                            }
                        }
                    } else {
                        if let g = group {
                            PensionLottoBall(number: g, size: 46)
                            Text("조")
                                .font(.system(size: 14))
                                .foregroundColor(.lottoGray500)
                        }
                        ForEach(0..<6, id: \.self) { index in
                            if index < numbers.count {
                                PensionLottoBall(number: numbers[index], size: 46)
                                    .transition(.scale.combined(with: .opacity))
                            } else {
                                Circle()
                                    .fill(Color.lottoGray100)
                                    .frame(width: 46, height: 46)
                                    .overlay {
                                        Text("?")
                                            .font(.system(size: 18, weight: .bold))
                                            .foregroundColor(.lottoGray300)
                                    }
                            }
                        }
                    }
                }
                .animation(.spring(duration: 0.3), value: numbers.count)
            }
            .padding(24)
            .background(Color(.secondarySystemGroupedBackground))
            .clipShape(RoundedRectangle(cornerRadius: 16))
            .padding(.horizontal, 16)

            Spacer()

            // Buttons
            VStack(spacing: 12) {
                Button(action: generate) {
                    Text("생성")
                        .font(.system(size: 16, weight: .bold))
                        .foregroundColor(.white)
                        .frame(maxWidth: .infinity)
                        .padding(.vertical, 14)
                        .background(isGenerating ? Color.lottoGray300 : Color.lottoGreen)
                        .clipShape(RoundedRectangle(cornerRadius: 12))
                }
                .disabled(isGenerating)

                Button(action: save) {
                    Text("저장")
                        .font(.system(size: 16, weight: .bold))
                        .foregroundColor(.white)
                        .frame(maxWidth: .infinity)
                        .padding(.vertical, 14)
                        .background(saveEnabled ? Color.lottoGreen : Color.lottoGray300)
                        .clipShape(RoundedRectangle(cornerRadius: 12))
                }
                .disabled(!saveEnabled)
            }
            .padding(.horizontal, 24)
            .padding(.bottom, 24)
        }
        .navigationTitle("랜덤 번호 생성")
        .navigationBarTitleDisplayMode(.inline)
        .overlay {
            if showSavedMessage {
                VStack {
                    Spacer()
                    HStack {
                        Text("저장되었습니다")
                            .foregroundColor(.white)
                        Spacer()
                        Button("내 번호 확인") {
                            showSavedMessage = false
                            router.pop()
                        }
                        .foregroundColor(.lottoGreen)
                    }
                    .padding(16)
                    .background(Color.lottoDarkSurface)
                    .clipShape(RoundedRectangle(cornerRadius: 12))
                    .padding(.horizontal, 16)
                    .padding(.bottom, 80)
                }
                .transition(.move(edge: .bottom))
            }
        }
        .onAppear {
            // TODO: Get round from UseCase
            round = isLotto645 ? 1166 : 226
            winningDate = "2026-04-04"
        }
    }

    private func generate() {
        isGenerating = true
        saveEnabled = false
        numbers = []
        group = isLotto645 ? nil : Int.random(in: 1...5)

        let totalCount = 6
        for i in 0..<totalCount {
            DispatchQueue.main.asyncAfter(deadline: .now() + Double(i) * 0.3) {
                withAnimation {
                    if isLotto645 {
                        let available = (1...45).filter { !numbers.contains($0) }
                        if let num = available.randomElement() {
                            numbers.append(num)
                        }
                    } else {
                        numbers.append(Int.random(in: 0...9))
                    }
                }
                if i == totalCount - 1 {
                    if isLotto645 { numbers.sort() }
                    isGenerating = false
                    saveEnabled = true
                }
            }
        }
    }

    private func save() {
        // TODO: Call InsertLotteryUseCase / InsertPensionLotteryUseCase via shared module
        saveEnabled = false
        withAnimation {
            showSavedMessage = true
        }
        DispatchQueue.main.asyncAfter(deadline: .now() + 3) {
            withAnimation { showSavedMessage = false }
        }
    }
}
