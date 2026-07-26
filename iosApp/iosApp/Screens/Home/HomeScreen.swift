import SwiftUI

struct HomeScreen: View {
    @State private var selectedTab = 0 // 0: Lotto 6/45, 1: Pension 7/20
    @State private var isLoading = false
    @State private var isError = false
    @State private var lotteryRound = 0
    @State private var pensionRound = 0
    @State private var lotteryNumbers: [Int] = []
    @State private var bonusNumber: Int?
    @State private var pensionNumbers: [Int] = []
    @State private var winningDate = ""

    var body: some View {
        ScrollView {
            VStack(spacing: 0) {
                // Tab Selector
                Picker("", selection: $selectedTab) {
                    Text("로또 6/45").tag(0)
                    Text("연금복권 720+").tag(1)
                }
                .pickerStyle(.segmented)
                .padding(.horizontal, 16)
                .padding(.top, 12)

                if isError {
                    ErrorRetryScreen(
                        title: "인터넷 연결이 불안정해요.",
                        description: "Wi-Fi나 셀룰러 데이터 연결 상태를\n확인하고 다시 시도해주세요.",
                        onRetry: { loadData() }
                    )
                    .frame(minHeight: 400)
                } else {
                    // Round Navigation
                    HStack {
                        Button(action: { changeRound(offset: -1) }) {
                            Image(systemName: "chevron.left")
                                .font(.system(size: 20, weight: .semibold))
                                .foregroundColor(.lottoGreen)
                        }

                        Spacer()

                        VStack(spacing: 4) {
                            let round = selectedTab == 0 ? lotteryRound : pensionRound
                            Text("\(round)회")
                                .font(.system(size: 22, weight: .bold))
                            if !winningDate.isEmpty {
                                Text(winningDate)
                                    .font(.system(size: 13))
                                    .foregroundColor(.lottoGray500)
                            }
                        }

                        Spacer()

                        Button(action: { changeRound(offset: 1) }) {
                            Image(systemName: "chevron.right")
                                .font(.system(size: 20, weight: .semibold))
                                .foregroundColor(.lottoGreen)
                        }
                    }
                    .padding(.horizontal, 24)
                    .padding(.vertical, 20)

                    // Lottery Numbers Display
                    if selectedTab == 0 {
                        lottoNumbersView
                    } else {
                        pensionNumbersView
                    }
                }

                Spacer(minLength: 50)
            }
        }
        .refreshable { loadData() }
        .overlay {
            if isLoading { LoadingDialog() }
        }
        .onAppear { loadData() }
    }

    private var lottoNumbersView: some View {
        VStack(spacing: 16) {
            if lotteryNumbers.isEmpty {
                Text("당첨번호가 아직 발표되지 않았습니다")
                    .font(.system(size: 15))
                    .foregroundColor(.lottoGray500)
                    .padding(.top, 40)
            } else {
                Text("당첨번호")
                    .font(.system(size: 14))
                    .foregroundColor(.lottoGray500)

                HStack(spacing: 6) {
                    ForEach(lotteryNumbers, id: \.self) { number in
                        LottoBall(number: number, size: 42)
                    }
                    if let bonus = bonusNumber {
                        Text("+")
                            .font(.system(size: 18, weight: .bold))
                            .foregroundColor(.lottoGray500)
                        LottoBall(number: bonus, size: 42)
                    }
                }
            }
        }
        .padding(.horizontal, 16)
    }

    private var pensionNumbersView: some View {
        VStack(spacing: 16) {
            if pensionNumbers.isEmpty {
                Text("당첨번호가 아직 발표되지 않았습니다")
                    .font(.system(size: 15))
                    .foregroundColor(.lottoGray500)
                    .padding(.top, 40)
            } else {
                Text("당첨번호")
                    .font(.system(size: 14))
                    .foregroundColor(.lottoGray500)

                HStack(spacing: 6) {
                    ForEach(pensionNumbers, id: \.self) { number in
                        PensionLottoBall(number: number, size: 42)
                    }
                }
            }
        }
        .padding(.horizontal, 16)
    }

    private func loadData() {
        // TODO: Call GetLotteryRoundUseCase / GetPensionLotteryRoundUseCase via shared module
        isLoading = true
        DispatchQueue.main.asyncAfter(deadline: .now() + 0.5) {
            lotteryRound = 1165
            pensionRound = 225
            winningDate = "2026-03-28"
            lotteryNumbers = [3, 12, 18, 27, 35, 42]
            bonusNumber = 7
            pensionNumbers = [9, 6, 0, 2, 1, 1]
            isLoading = false
        }
    }

    private func changeRound(offset: Int) {
        if selectedTab == 0 {
            lotteryRound += offset
        } else {
            pensionRound += offset
        }
        loadData()
    }
}
