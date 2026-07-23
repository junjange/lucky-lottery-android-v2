package junjange.core.data.repository

import junjange.core.data.datasource.LotteryDataSource
import junjange.core.data.datasource.PensionLotteryRoomDataSource
import junjange.core.data.mapper.toBonusCorrectNumbers
import junjange.core.data.mapper.toCorrectNumbers
import junjange.core.data.mapper.toDomain
import junjange.core.data.mapper.toWinningPensionLotteryBonusNumbers
import junjange.core.data.mapper.toWinningPensionLotteryNumbers
import junjange.core.data.model.local.PensionLotteryNumberDto
import junjange.core.domain.model.PensionLotteryGetContent
import junjange.core.domain.model.PensionLotteryHome
import junjange.core.domain.model.PensionLotteryNumbers
import junjange.core.domain.model.PensionLotteryRandom
import junjange.core.domain.repository.PensionLotteryRepository
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.todayIn

internal class PensionLotteryRepositoryImpl
    
    constructor(
        private val lotteryDataSource: LotteryDataSource,
        private val pensionLotteryRoomDataSource: PensionLotteryRoomDataSource,
    ) : PensionLotteryRepository {
        private val pensionLottery: LinkedHashMap<Int, PensionLotteryHome> = linkedMapOf()

        private val nextRound
            get() = pensionLottery.keys.firstOrNull()?.plus(1)

        private val nextWinningDate
            get() = addDaysToDate(pensionLottery.values.firstOrNull()?.winningDate)

        override suspend fun getPensionLotteryRandom(): Result<PensionLotteryRandom> =
            runCatching {
                val pensionRound =
                    nextRound ?: run {
                        getPensionLotteryRound()
                            .map { it + 1 }
                            .getOrElse { throw it }
                    }

                val randomNumbers = generateNumbers()
                val group = generateGroupNumber()
                PensionLotteryRandom(
                    pensionRound = pensionRound,
                    pensionGroup = group,
                    pensionFirstNum = randomNumbers[0],
                    pensionSecondNum = randomNumbers[1],
                    pensionThirdNum = randomNumbers[2],
                    pensionFourthNum = randomNumbers[3],
                    pensionFifthNum = randomNumbers[4],
                    pensionSixthNum = randomNumbers[5],
                )
            }

        private fun generateGroupNumber(): Int = (1..5).shuffled().first()

        private fun generateNumbers(): List<Int> = (0..9).shuffled().take(6)

        override suspend fun getPensionLotteryRound(): Result<Int> = lotteryDataSource.getPensionLotteryRound()

        override suspend fun getPensionLottoNumber(drwNo: Int): Result<PensionLotteryHome> {
            pensionLottery[drwNo]?.let {
                return Result.success(it)
            }

            val pensionLotteryHome =
                lotteryDataSource
                    .getPensionLottoNumber(drwNo = drwNo)
                    .mapCatching { it.toDomain() }

            if (pensionLotteryHome.isSuccess) {
                pensionLottery[drwNo] = pensionLotteryHome.getOrThrow()
            }

            return pensionLotteryHome
        }

        override suspend fun loadPensionLotteryRounds(
            page: Int,
            size: Int,
        ): Result<List<PensionLotteryGetContent>> {
            val pagedRounds =
                pensionLotteryRoomDataSource
                    .getPagedRounds(limit = size, offset = page * size)
                    .getOrThrow()

            if (pagedRounds.isEmpty()) return Result.success(emptyList())

            val lotteries =
                pensionLotteryRoomDataSource
                    .getPensionLotteriesByRound(pagedRounds)
                    .getOrThrow()

            return getWinningPensionLotteries(pagedRounds, lotteries)
        }

        override suspend fun insertPensionLottery(
            group: Int,
            firstNum: Int,
            secondNum: Int,
            thirdNum: Int,
            fourthNum: Int,
            fifthNum: Int,
            sixthNum: Int,
        ): Result<Unit> {
            val round =
                nextRound ?: run {
                    getPensionLotteryRound()
                        .map { it + 1 }
                        .getOrElse { return Result.failure(it) }
                }

            val pensionLotteryNumberDto =
                PensionLotteryNumberDto(
                    round = round,
                    group = group,
                    firstNum = firstNum,
                    secondNum = secondNum,
                    thirdNum = thirdNum,
                    fourthNum = fourthNum,
                    fifthNum = fifthNum,
                    sixthNum = sixthNum,
                )
            return pensionLotteryRoomDataSource.insertPensionLottery(pensionLotteryNumberDto = pensionLotteryNumberDto)
        }

        override suspend fun deletePensionLotteryByRoundAndId(
            round: Int,
            id: Long,
        ): Result<Unit> =
            pensionLotteryRoomDataSource.deletePensionLotteryByRoundAndId(
                round = round,
                id = id,
            )

        override suspend fun deleteAllPensionLottery(): Result<Unit> = pensionLotteryRoomDataSource.deleteAllPensionLottery()

        private suspend fun getWinningPensionLotteries(
            pagedRounds: List<Int>,
            pensionLotteries: List<PensionLotteryNumberDto>,
        ): Result<List<PensionLotteryGetContent>> =
            runCatching {
                pagedRounds.map { round ->
                    val pensionLotteryNumbers = getPensionLottoNumber(drwNo = round).getOrNull()
                    val winningLotteryNumbers =
                        pensionLotteryNumbers?.toWinningPensionLotteryNumbers()
                    val winningPensionLotteryBonusNumbers =
                        pensionLotteryNumbers?.toWinningPensionLotteryBonusNumbers()
                    val winningDate = pensionLotteryNumbers?.winningDate ?: nextWinningDate

                    PensionLotteryGetContent(
                        round = round,
                        winningDate = winningDate,
                        checkWinningBonus = false,
                        winningPensionLotteryBonusNumbers = null,
                        winningPensionLotteryNumbers = winningLotteryNumbers,
                        pensionLotteryNumbers =
                            pensionLotteries.filter { it.round == round }.map { pensionLottery ->
                                val correctNumbers =
                                    winningLotteryNumbers?.toCorrectNumbers(pensionLottery)
                                val bonusCorrectNumbers =
                                    winningPensionLotteryBonusNumbers?.toBonusCorrectNumbers(
                                        pensionLottery,
                                    )
                                val checkWinningBonus = bonusCorrectNumbers?.all { it } ?: false

                                val rank =
                                    when (correctNumbers?.count { it }) {
                                        6 -> {
                                            if (winningLotteryNumbers.group == pensionLottery.group) {
                                                "FIRST"
                                            } else {
                                                "SECOND"
                                            }
                                        }

                                        5 -> "THIRD"
                                        4 -> "FOURTH"
                                        3 -> "FIFTH"
                                        2 -> "SIXTH"
                                        1 -> "SEVENTH"
                                        0 -> "NONE"
                                        else -> "미발표"
                                    }

                                PensionLotteryNumbers(
                                    id = pensionLottery.id,
                                    group = pensionLottery.group,
                                    firstNum = pensionLottery.firstNum,
                                    secondNum = pensionLottery.secondNum,
                                    thirdNum = pensionLottery.thirdNum,
                                    fourthNum = pensionLottery.fourthNum,
                                    fifthNum = pensionLottery.fifthNum,
                                    sixthNum = pensionLottery.sixthNum,
                                    rank = rank,
                                    checkWinningBonus = checkWinningBonus,
                                    correctNumbers = correctNumbers,
                                    bonusCorrectNumbers = bonusCorrectNumbers,
                                )
                            },
                    )
                }
            }

        private fun addDaysToDate(dateStr: String?): String {
            if (dateStr != null) {
                val date = LocalDate.parse(dateStr)
                return date.plus(7, DateTimeUnit.DAY).toString()
            } else {
                val today = Clock.System.todayIn(TimeZone.of("Asia/Seoul"))
                val dayOfWeek = today.dayOfWeek
                val daysUntilThursday = (DayOfWeek.THURSDAY.ordinal - dayOfWeek.ordinal + 7) % 7
                return today.plus(daysUntilThursday, DateTimeUnit.DAY).toString()
            }
        }
    }
