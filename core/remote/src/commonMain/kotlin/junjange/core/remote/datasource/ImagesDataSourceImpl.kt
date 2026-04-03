package junjange.core.remote.datasource

import io.ktor.client.request.forms.MultiPartFormDataContent
import junjange.core.data.datasource.ImagesDataSource
import junjange.core.data.model.remote.ImageUploadEntity
import junjange.core.remote.api.ApiService
import junjange.core.remote.model.response.toData

internal class ImagesDataSourceImpl

    constructor(
        private val apiService: ApiService,
    ) : ImagesDataSource {
        override suspend fun postImagesUpload(file: MultiPartFormDataContent): Result<ImageUploadEntity> =
            runCatching {
                apiService.postImagesUpload(file = file).data.toData()
            }
    }
