package com.junjange.presentation.ui.mynumber

import android.os.Parcelable
import androidx.paging.PagingData
import junjange.core.domain.model.LotteryGetContent
import junjange.core.domain.model.PensionLotteryGetContent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.parcelize.Parcelize

sealed interface MyNumberContract {
    data class State(
        val isLoading: Boolean = false,
        val isDeleteLotteryDialogShowing: Boolean = false,
        val lotteryFlow: Flow<PagingData<LotteryGetContent>> = emptyFlow(),
        val pensionLotteryFlow: Flow<PagingData<PensionLotteryGetContent>> = emptyFlow(),
    )

    @Parcelize
    data class UserRoundId(
        val round: Int,
        val id: Long,
    ) : Parcelable

    sealed interface Event {
        data object PickedImage : Event

        data object LoadLottery : Event

        data object LoadPensionLottery : Event

        data class InsertLottery(
            val lottery: List<String>,
        ) : Event

        data class InsertPensionLottery(
            val pensionLottery: List<String>,
        ) : Event

        data class LottoTextOfImage(
            val imagePath: String,
        ) : Event

        data class PensionLottoTextOfImage(
            val imagePath: String,
        ) : Event

        data class ShowDialog(
            val isDialogShowing: Boolean,
        ) : Event

        data class DeleteLottery(
            val userRoundIds: List<UserRoundId>,
        ) : Event

        data class DeletePensionLottery(
            val userRoundIds: List<UserRoundId>,
        ) : Event
    }

    sealed interface Effect {
        data object NavigateToGallery : Effect

        data object LotteryRefresh : Effect

        data object PensionLotteryRefresh : Effect
    }
}

fun String?.toRankTitle(): String =
    when (this) {
        "FIRST" -> "1등"
        "SECOND" -> "2등"
        "THIRD" -> "3등"
        "FOURTH" -> "4등"
        "FIFTH" -> "5등"
        "SIXTH" -> "6등"
        "SEVENTH" -> "7등"
        "NONE" -> "꽝"
        else -> "미발표"
    }

fun String.extractLottoNumbers(): List<List<String>> = this.split("\n").map { it.split(" ") }

fun String.extractPensionLottoNumbers(): List<List<String>> =
    this.split("\n").map {
        it.replace(" ", "").replace("조", "").map { it.toString() }
    }

fun List<List<String>>.isValidLottoNumbers(): Boolean =
    all { lottoNumbers ->
        lottoNumbers.size == 6 &&
            lottoNumbers.all { lottoNumber ->
                lottoNumber.toIntOrNull() != null && lottoNumber.toInt() in 1..45
            }
    }

fun List<List<String>>.isValidPensionLottoNumbers(): Boolean =
    all { lottoNumbers ->
        lottoNumbers.size == 7 &&
            lottoNumbers.all { lottoNumber ->
                lottoNumber.toIntOrNull() != null && lottoNumber.toInt() in 0..9
            }
    }
