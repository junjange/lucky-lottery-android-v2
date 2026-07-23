import Foundation
import LuckyLotteryShared
import PhotosUI
import UIKit

/// Kotlin(core/ui iosMain)의 IosImagePickerBridge에 PHPicker 기반 이미지 선택기를 주입한다.
/// 선택한 이미지를 임시 파일(JPEG)로 저장해 경로를 돌려준다 (Vision OCR 입력용).
enum PhotoPickerBridgeSetup {
    private static var activeDelegate: PickerDelegate?

    static func register() {
        IosImagePickerBridge.shared.pickImage = { onResult in
            guard let root = AdBridgeSetup.rootViewController() else {
                onResult(nil)
                return
            }

            var configuration = PHPickerConfiguration()
            configuration.filter = .images
            configuration.selectionLimit = 1

            let delegate = PickerDelegate { path in
                activeDelegate = nil
                onResult(path)
            }
            activeDelegate = delegate

            let picker = PHPickerViewController(configuration: configuration)
            picker.delegate = delegate
            root.present(picker, animated: true)
        }
    }

    private final class PickerDelegate: NSObject, PHPickerViewControllerDelegate {
        private let onResult: (String?) -> Void

        init(onResult: @escaping (String?) -> Void) {
            self.onResult = onResult
        }

        func picker(_ picker: PHPickerViewController, didFinishPicking results: [PHPickerResult]) {
            picker.dismiss(animated: true)

            guard let provider = results.first?.itemProvider,
                  provider.canLoadObject(ofClass: UIImage.self)
            else {
                onResult(nil)
                return
            }

            provider.loadObject(ofClass: UIImage.self) { [onResult] object, _ in
                DispatchQueue.main.async {
                    guard let image = object as? UIImage,
                          let data = image.jpegData(compressionQuality: 0.9)
                    else {
                        onResult(nil)
                        return
                    }
                    let path = NSTemporaryDirectory() + "lottery_ocr_\(UUID().uuidString).jpg"
                    do {
                        try data.write(to: URL(fileURLWithPath: path))
                        onResult(path)
                    } catch {
                        onResult(nil)
                    }
                }
            }
        }
    }
}
