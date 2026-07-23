package junjange.core.ocr.service

import kotlinx.cinterop.ExperimentalForeignApi
import platform.UIKit.UIImage
import platform.Vision.VNImageRequestHandler
import platform.Vision.VNRecognizeTextRequest
import platform.Vision.VNRecognizedTextObservation

@OptIn(ExperimentalForeignApi::class)
internal class IosOcrService : OcrService {
    override fun getTextOfImage(imagePath: String): String {
        val cgImage = UIImage(contentsOfFile = imagePath)?.CGImage ?: return ""

        val builder = StringBuilder()
        val request =
            VNRecognizeTextRequest { request, _ ->
                request?.results.orEmpty().forEach { observation ->
                    (observation as? VNRecognizedTextObservation)
                        ?.topCandidates(1u)
                        ?.firstOrNull()
                        ?.let { candidate ->
                            val text = (candidate as? platform.Vision.VNRecognizedText)?.string
                            if (text != null) builder.append(text).append("\n")
                        }
                }
            }.apply {
                recognitionLanguages = listOf("ko-KR", "en-US")
                usesLanguageCorrection = false
            }

        val handler = VNImageRequestHandler(cGImage = cgImage, options = emptyMap<Any?, Any?>())
        handler.performRequests(listOf(request), null)

        return builder.toString()
    }
}
