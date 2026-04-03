import SwiftUI
import LuckyLotteryShared

struct ContentView: View {
    var body: some View {
        NavigationStack {
            VStack(spacing: 20) {
                Image(systemName: "clover.fill")
                    .resizable()
                    .frame(width: 80, height: 80)
                    .foregroundColor(.green)

                Text("행운 복권")
                    .font(.largeTitle)
                    .fontWeight(.bold)

                Text("Lucky Lottery")
                    .font(.title3)
                    .foregroundColor(.secondary)
            }
            .navigationTitle("홈")
        }
    }
}

#Preview {
    ContentView()
}
