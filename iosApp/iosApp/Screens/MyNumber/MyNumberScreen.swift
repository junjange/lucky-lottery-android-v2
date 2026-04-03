import SwiftUI

struct MyNumberScreen: View {
    @State private var selectedTab = 0
    @State private var lotteryItems: [LotteryItem] = []
    @State private var pensionItems: [PensionLotteryItem] = []
    @State private var showNumberEntry = false
    @State private var isDeleteMode = false
    @State private var selectedIds: Set<String> = []
    @State private var showDeleteDialog = false

    var body: some View {
        VStack(spacing: 0) {
            // Tab
            Picker("", selection: $selectedTab) {
                Text("로또 6/45").tag(0)
                Text("연금복권 720+").tag(1)
            }
            .pickerStyle(.segmented)
            .padding(.horizontal, 16)
            .padding(.top, 8)

            // Content
            if selectedTab == 0 {
                lotteryListView
            } else {
                pensionListView
            }
        }
        .overlay(alignment: .bottomTrailing) {
            fabMenu
        }
        .sheet(isPresented: $showNumberEntry) {
            numberEntrySheet
        }
        .confirmationDialog("삭제하시겠습니까?", isPresented: $showDeleteDialog, titleVisibility: .visible) {
            Button("삭제", role: .destructive) { deleteSelected() }
            Button("취소", role: .cancel) {}
        }
        .onAppear { loadData() }
    }

    // MARK: - Lottery List
    private var lotteryListView: some View {
        Group {
            if lotteryItems.isEmpty {
                EmptyScreen(message: "저장된 번호가 없습니다")
            } else {
                List {
                    ForEach(lotteryItems) { item in
                        lotteryRow(item)
                    }
                }
                .listStyle(.plain)
            }
        }
    }

    private func lotteryRow(_ item: LotteryItem) -> some View {
        HStack(spacing: 0) {
            if isDeleteMode {
                Image(systemName: selectedIds.contains(item.id) ? "checkmark.circle.fill" : "circle")
                    .foregroundColor(selectedIds.contains(item.id) ? .lottoGreen : .lottoGray300)
                    .padding(.trailing, 8)
                    .onTapGesture { toggleSelection(item.id) }
            }

            VStack(alignment: .leading, spacing: 8) {
                Text("\(item.round)회")
                    .font(.system(size: 13))
                    .foregroundColor(.lottoGray500)
                HStack(spacing: 4) {
                    ForEach(item.numbers, id: \.self) { number in
                        LottoBall(number: number, size: 32)
                    }
                }
                if let rank = item.rank, rank != "미발표" {
                    Text(rank)
                        .font(.system(size: 12, weight: .bold))
                        .foregroundColor(rank == "NONE" ? .lottoGray500 : .lottoGreen)
                }
            }
        }
        .padding(.vertical, 4)
    }

    // MARK: - Pension List
    private var pensionListView: some View {
        Group {
            if pensionItems.isEmpty {
                EmptyScreen(message: "저장된 번호가 없습니다")
            } else {
                List {
                    ForEach(pensionItems) { item in
                        pensionRow(item)
                    }
                }
                .listStyle(.plain)
            }
        }
    }

    private func pensionRow(_ item: PensionLotteryItem) -> some View {
        VStack(alignment: .leading, spacing: 8) {
            Text("\(item.round)회")
                .font(.system(size: 13))
                .foregroundColor(.lottoGray500)
            HStack(spacing: 4) {
                PensionLottoBall(number: item.group, size: 32)
                Text("조")
                    .font(.system(size: 12))
                    .foregroundColor(.lottoGray500)
                ForEach(item.numbers, id: \.self) { number in
                    PensionLottoBall(number: number, size: 32)
                }
            }
        }
        .padding(.vertical, 4)
    }

    // MARK: - FAB Menu
    private var fabMenu: some View {
        VStack(spacing: 12) {
            if isDeleteMode {
                Button(action: { showDeleteDialog = true }) {
                    Image(systemName: "trash.fill")
                        .font(.system(size: 18))
                        .foregroundColor(.white)
                        .frame(width: 50, height: 50)
                        .background(Color.red)
                        .clipShape(Circle())
                }
                Button(action: { isDeleteMode = false; selectedIds.removeAll() }) {
                    Image(systemName: "xmark")
                        .font(.system(size: 18))
                        .foregroundColor(.white)
                        .frame(width: 50, height: 50)
                        .background(Color.lottoGray500)
                        .clipShape(Circle())
                }
            } else {
                Button(action: { showNumberEntry = true }) {
                    Image(systemName: "pencil")
                        .font(.system(size: 18))
                        .foregroundColor(.white)
                        .frame(width: 50, height: 50)
                        .background(Color.lottoGreen)
                        .clipShape(Circle())
                }
                Button(action: { isDeleteMode = true }) {
                    Image(systemName: "trash")
                        .font(.system(size: 18))
                        .foregroundColor(.white)
                        .frame(width: 50, height: 50)
                        .background(Color.lottoGreen)
                        .clipShape(Circle())
                }
            }
        }
        .padding(16)
        .shadow(radius: 4)
    }

    // MARK: - Number Entry Sheet
    private var numberEntrySheet: some View {
        NavigationStack {
            NumberEntryView(isLotto645: selectedTab == 0) { numbers in
                if selectedTab == 0 {
                    // TODO: Call InsertLotteryUseCase
                } else {
                    // TODO: Call InsertPensionLotteryUseCase
                }
                showNumberEntry = false
                loadData()
            }
            .navigationTitle(selectedTab == 0 ? "로또 번호 입력" : "연금복권 번호 입력")
            .navigationBarTitleDisplayMode(.inline)
            .toolbar {
                ToolbarItem(placement: .cancellationAction) {
                    Button("취소") { showNumberEntry = false }
                }
            }
        }
    }

    // MARK: - Actions
    private func toggleSelection(_ id: String) {
        if selectedIds.contains(id) {
            selectedIds.remove(id)
        } else {
            selectedIds.insert(id)
        }
    }

    private func deleteSelected() {
        // TODO: Call DeleteLotteryByRoundAndIdUseCase
        selectedIds.removeAll()
        isDeleteMode = false
        loadData()
    }

    private func loadData() {
        // TODO: Call LoadLotteryRoundsUseCase / LoadPensionLotteryRoundsUseCase via shared module
    }
}

// MARK: - Number Entry View
struct NumberEntryView: View {
    let isLotto645: Bool
    let onSave: ([Int]) -> Void

    @State private var numbers: [String]

    init(isLotto645: Bool, onSave: @escaping ([Int]) -> Void) {
        self.isLotto645 = isLotto645
        self.onSave = onSave
        _numbers = State(initialValue: Array(repeating: "", count: isLotto645 ? 6 : 7))
    }

    var body: some View {
        VStack(spacing: 20) {
            ForEach(0..<numbers.count, id: \.self) { index in
                HStack {
                    Text(index == 0 && !isLotto645 ? "조" : "\(index + (isLotto645 ? 1 : 0))번째")
                        .font(.system(size: 14))
                        .foregroundColor(.lottoGray500)
                        .frame(width: 50)
                    TextField("", text: $numbers[index])
                        .keyboardType(.numberPad)
                        .textFieldStyle(.roundedBorder)
                }
            }

            Spacer()

            Button(action: {
                let parsed = numbers.compactMap { Int($0) }
                if parsed.count == numbers.count {
                    onSave(parsed)
                }
            }) {
                Text("저장")
                    .font(.system(size: 16, weight: .bold))
                    .foregroundColor(.white)
                    .frame(maxWidth: .infinity)
                    .padding(.vertical, 14)
                    .background(Color.lottoGreen)
                    .clipShape(RoundedRectangle(cornerRadius: 12))
            }
        }
        .padding(24)
    }
}

// MARK: - Models
struct LotteryItem: Identifiable {
    let id: String
    let round: Int
    let numbers: [Int]
    let rank: String?
}

struct PensionLotteryItem: Identifiable {
    let id: String
    let round: Int
    let group: Int
    let numbers: [Int]
    let rank: String?
}
