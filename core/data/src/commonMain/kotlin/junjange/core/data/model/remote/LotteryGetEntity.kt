package junjange.core.data.model.remote

data class LotteryGetEntity(
    val content: List<LotteryGetContentEntity>,
    val last: Boolean,
)
