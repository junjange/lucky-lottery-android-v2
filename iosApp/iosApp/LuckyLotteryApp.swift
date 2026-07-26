import SwiftUI
import LuckyLotteryShared

@main
struct LuckyLotteryApp: App {
    init() {
        KoinHelperKt.startKoinApp()
        AdBridgeSetup.register()
        QrScannerBridgeSetup.register()
        PhotoPickerBridgeSetup.register()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
