package com.junjange.presentation.ui.mynumber

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.junjange.domain.model.LotteryGetContent
import com.junjange.domain.usecase.LoadLotteryRoundsUseCase

fun createLotteryPagingSource(loadLotteryRoundsUseCase: LoadLotteryRoundsUseCase): Pager<Int, LotteryGetContent> =
    Pager(
        config =
            PagingConfig(
                pageSize = 10,
                initialLoadSize = 30,
                enablePlaceholders = true,
            ),
        initialKey = 0,
        pagingSourceFactory = { LotteryPagingSource(loadLotteryRoundsUseCase = loadLotteryRoundsUseCase) },
    )

class LotteryPagingSource(
    private val loadLotteryRoundsUseCase: LoadLotteryRoundsUseCase,
) : PagingSource<Int, LotteryGetContent>() {
    override fun getRefreshKey(state: PagingState<Int, LotteryGetContent>): Int? = state.anchorPosition

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LotteryGetContent> {
        val pageIndex = params.key ?: 0

        val result =
            loadLotteryRoundsUseCase(
                page = pageIndex,
                size = params.loadSize,
            )

        return result.fold(
            onSuccess = {
                LoadResult.Page(
                    data = it,
                    prevKey = null,
                    nextKey = if (it.isEmpty()) null else pageIndex + 1,
                )
            },
            onFailure = {
                LoadResult.Error(it)
            },
        )
    }
}
