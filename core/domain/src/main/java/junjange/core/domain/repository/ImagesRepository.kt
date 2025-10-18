package junjange.core.domain.repository

import junjange.core.domain.model.ImageUpload
import okhttp3.MultipartBody

interface ImagesRepository {
    suspend fun postImagesUpload(file: MultipartBody.Part): Result<ImageUpload>
}
