package junjange.core.data.datasource

import junjange.core.data.model.remote.ImageUploadEntity
import okhttp3.MultipartBody

interface ImagesDataSource {
    suspend fun postImagesUpload(file: MultipartBody.Part): Result<ImageUploadEntity>
}
