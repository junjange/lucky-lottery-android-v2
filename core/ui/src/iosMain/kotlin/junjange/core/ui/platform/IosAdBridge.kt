package junjange.core.ui.platform

/**
 * Swift에서 구현을 주입하는 이미지 피커 브리지.
 * pickImage 호출 시 PHPicker를 모달로 띄우고, 선택한 이미지의 임시 파일 경로(취소 시 null)를 콜백으로 돌려준다.
 */
object IosImagePickerBridge {
    var pickImage: ((onResult: (String?) -> Unit) -> Unit)? = null
}
