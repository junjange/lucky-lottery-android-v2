package junjange.core.domain.repository

import io.ktor.client.request.forms.MultiPartFormDataContent
import junjange.core.domain.model.ImageUpload

interface ImagesRepository {
    suspend fun postImagesUpload(file: MultiPartFormDataContent): Result<ImageUpload>
}
