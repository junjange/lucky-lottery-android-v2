package junjange.core.remote.model.response

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

import junjange.core.data.model.remote.ImageUploadEntity

@Serializable
data class ImagesUploadResponse(
    val imageUrl: String,
)

fun ImagesUploadResponse.toData(): ImageUploadEntity = ImageUploadEntity(imageUrl = imageUrl)
