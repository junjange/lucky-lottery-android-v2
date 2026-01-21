package junjange.core.data.repository

import junjange.core.data.datasource.ImagesDataSource
import junjange.core.data.mapper.toDomain
import junjange.core.domain.model.ImageUpload
import junjange.core.domain.repository.ImagesRepository
import okhttp3.MultipartBody

internal class ImagesRepositoryImpl
    
    constructor(
        private val imagesDataSource: ImagesDataSource,
    ) : ImagesRepository {
        override suspend fun postImagesUpload(file: MultipartBody.Part): Result<ImageUpload> =
            imagesDataSource.postImagesUpload(file = file).mapCatching { it.toDomain() }
    }
