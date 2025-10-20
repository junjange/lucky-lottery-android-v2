package junjange.core.data.mapper

import junjange.core.data.model.remote.ImageUploadEntity
import junjange.core.domain.model.ImageUpload

internal fun ImageUploadEntity.toDomain(): ImageUpload = ImageUpload(imageUrl = imageUrl)
