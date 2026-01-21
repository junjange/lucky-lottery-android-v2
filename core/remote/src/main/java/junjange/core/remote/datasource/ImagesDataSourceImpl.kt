package junjange.core.remote.datasource

import junjange.core.data.datasource.ImagesDataSource
import junjange.core.data.model.remote.ImageUploadEntity
import junjange.core.remote.api.ApiService
import junjange.core.remote.model.response.toData
import okhttp3.MultipartBody

internal class ImagesDataSourceImpl
    
    constructor(
        private val apiService: ApiService,
    ) : ImagesDataSource {
        override suspend fun postImagesUpload(file: MultipartBody.Part): Result<ImageUploadEntity> =
            runCatching {
                apiService.postImagesUpload(file = file).data.toData()
            }
    }
