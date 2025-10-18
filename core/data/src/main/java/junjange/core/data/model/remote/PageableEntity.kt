package junjange.core.data.model.remote

data class PageableEntity(
    val offset: Int,
    val sort: SortEntity,
    val pageSize: Int,
    val paged: Boolean,
    val pageNumber: Int,
    val unpaged: Boolean,
)
