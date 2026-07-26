package junjange.core.ocr.service

/**
 * Extracts text from an image on disk.
 *
 * Android is backed by Tesseract (tesseract4android); iOS uses the Apple Vision
 * framework (`VNRecognizeTextRequest`).
 */
interface OcrService {
    fun getTextOfImage(imagePath: String): String
}
