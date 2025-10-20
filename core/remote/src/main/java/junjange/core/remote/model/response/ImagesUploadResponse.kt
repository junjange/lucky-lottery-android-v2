package junjange.core.remote.model.response

import junjange.core.data.model.remote.ImageUploadEntity

data class ImagesUploadResponse(
    val imageUrl: String,
)

fun ImagesUploadResponse.toData(): ImageUploadEntity = ImageUploadEntity(imageUrl = imageUrl)
