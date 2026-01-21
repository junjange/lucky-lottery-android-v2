package junjange.core.data.datasource

import io.ktor.client.request.forms.MultiPartFormDataContent
import junjange.core.data.model.remote.ImageUploadEntity

interface ImagesDataSource {
    suspend fun postImagesUpload(file: MultiPartFormDataContent): Result<ImageUploadEntity>
}
