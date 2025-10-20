package junjange.core.domain.model

data class LotteryGet(
    val content: List<LotteryGetContent>,
    val last: Boolean,
)
