package com.junjange.presentation.ui.mynumber

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.junjange.domain.model.PensionLotteryGetContent
import com.junjange.domain.usecase.LoadPensionLotteryRoundsUseCase

fun createPensionLotteryPagingSource(
    loadPensionLotteryRoundsUseCase: LoadPensionLotteryRoundsUseCase,
): Pager<Int, PensionLotteryGetContent> =
    Pager(
        config =
            PagingConfig(
                pageSize = 10,
                initialLoadSize = 30,
                enablePlaceholders = true,
            ),
        initialKey = 0,
        pagingSourceFactory = { PensionLotteryPagingSource(loadPensionLotteryRoundsUseCase = loadPensionLotteryRoundsUseCase) },
    )

class PensionLotteryPagingSource(
    private val loadPensionLotteryRoundsUseCase: LoadPensionLotteryRoundsUseCase,
) : PagingSource<Int, PensionLotteryGetContent>() {
    override fun getRefreshKey(state: PagingState<Int, PensionLotteryGetContent>): Int? =
        state.anchorPosition?.let { state.closestPageToPosition(it)?.prevKey?.plus(1) }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PensionLotteryGetContent> {
        val pageIndex = params.key ?: 0

        val result =
            loadPensionLotteryRoundsUseCase(
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
