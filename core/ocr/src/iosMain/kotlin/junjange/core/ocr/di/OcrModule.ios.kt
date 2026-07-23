package junjange.core.ocr.di

import junjange.core.ocr.service.IosOcrService
import junjange.core.ocr.service.OcrService
import org.koin.core.module.Module
import org.koin.dsl.module

actual val ocrModule: Module = module {
    single<OcrService> { IosOcrService() }
}
