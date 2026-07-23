package junjange.feature.mynumber

import androidx.compose.runtime.Composable

/**
 * 복권 용지 사진을 선택해 파일 경로로 돌려주는 런처.
 * Android: 갤러리 선택 후 크롭(OCR 정확도 향상), iOS: PHPicker 선택(Vision OCR이 전체 이미지 처리).
 */
@Composable
expect fun rememberLotteryImagePicker(onImagePicked: (imagePath: String) -> Unit): () -> Unit
