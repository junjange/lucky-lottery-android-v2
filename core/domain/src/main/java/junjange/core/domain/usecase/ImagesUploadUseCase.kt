package junjange.core.domain.usecase

import io.ktor.client.request.forms.MultiPartFormDataContent
import junjange.core.domain.model.ImageUpload
import junjange.core.domain.repository.ImagesRepository

class ImagesUploadUseCase

    constructor(
        private val repository: ImagesRepository,
    ) {
        suspend operator fun invoke(file: MultiPartFormDataContent): Result<ImageUpload> = repository.postImagesUpload(file = file)
    }
