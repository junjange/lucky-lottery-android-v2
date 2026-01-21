package junjange.core.data.repository

import io.ktor.client.request.forms.MultiPartFormDataContent
import junjange.core.data.datasource.ImagesDataSource
import junjange.core.data.mapper.toDomain
import junjange.core.domain.model.ImageUpload
import junjange.core.domain.repository.ImagesRepository

internal class ImagesRepositoryImpl

    constructor(
        private val imagesDataSource: ImagesDataSource,
    ) : ImagesRepository {
        override suspend fun postImagesUpload(file: MultiPartFormDataContent): Result<ImageUpload> =
            imagesDataSource.postImagesUpload(file = file).mapCatching { it.toDomain() }
    }
