package junjange.core.domain.usecase

import junjange.core.domain.model.ImageUpload
import junjange.core.domain.repository.ImagesRepository
import okhttp3.MultipartBody
import javax.inject.Inject

class ImagesUploadUseCase
    @Inject
    constructor(
        private val repository: ImagesRepository,
    ) {
        suspend operator fun invoke(file: MultipartBody.Part): Result<ImageUpload> = repository.postImagesUpload(file = file)
    }
