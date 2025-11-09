package junjange.core.data.repository

import junjange.core.data.datasource.LotteryDataSource
import junjange.core.data.datasource.LotteryRoomDataSource
import junjange.core.data.mapper.toCorrectNumbers
import junjange.core.data.mapper.toDomain
import junjange.core.data.mapper.toWinningLotteryNumbers
import junjange.core.data.model.local.LotteryNumberDto
import junjange.core.domain.model.LotteryGet
import junjange.core.domain.model.LotteryGetContent
import junjange.core.domain.model.LotteryGetNumbers
import junjange.core.domain.model.LotteryNumbers
import junjange.core.domain.model.LotteryRandomNumbers
import junjange.core.domain.repository.LotteryRepository
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject

internal class LotteryRepositoryImpl
    @Inject
    constructor(
        private val lotteryDataSource: LotteryDataSource,
        private val lotteryRoomDataSource: LotteryRoomDataSource,
    ) : LotteryRepository {
        private val lottery: LinkedHashMap<Int, LotteryNumbers> = linkedMapOf()

        private val nextLotteryRound
            get() = lottery.keys.firstOrNull()?.plus(1)

        private val nextLotteryWinningDate
            get() = addDaysToDate(lottery.values.firstOrNull()?.winningDate)

        override suspend fun loadLotteryRounds(
            page: Int,
            size: Int,
        ): Result<List<LotteryGetContent>> {
            val pagedRounds =
                lotteryRoomDataSource
                    .getPagedRounds(limit = size, offset = page * size)
                    .getOrThrow()

            if (pagedRounds.isEmpty()) return Result.success(emptyList())

            val lotteries =
                lotteryRoomDataSource.getLotteriesByRound(pagedRounds).getOrThrow()

            return getWinningLotteries(pagedRounds, lotteries)
        }

        override suspend fun insertLottery(
            firstNum: Int,
            secondNum: Int,
            thirdNum: Int,
            fourthNum: Int,
            fifthNum: Int,
            sixthNum: Int,
        ): Result<Unit> {
            val round =
                nextLotteryRound ?: run {
                    getLotteryRound()
                        .map { it + 1 }
                        .getOrElse { return Result.failure(it) }
                }

            val lotteryNumberDto =
                LotteryNumberDto(
                    round = round,
                    firstNum = firstNum,
                    secondNum = secondNum,
                    thirdNum = thirdNum,
                    fourthNum = fourthNum,
                    fifthNum = fifthNum,
                    sixthNum = sixthNum,
                )
            return lotteryRoomDataSource.insertLottery(lotteryNumberDto = lotteryNumberDto)
        }

        override suspend fun deleteLotteryByRoundAndId(
            round: Int,
            id: Long,
        ): Result<Unit> = lotteryRoomDataSource.deleteLotteryByRoundAndId(round = round, id = id)

        private suspend fun getWinningLotteries(
            pagedRounds: List<Int>,
            lotteries: List<LotteryNumberDto>,
        ): Result<List<LotteryGetContent>> =
            runCatching {
                pagedRounds.map { round ->
                    val lotteryNumbers = getLottoNumber(drwNo = round).getOrNull()
                    val winningLotteryNumbers = lotteryNumbers?.toWinningLotteryNumbers()
                    val winningDate = lotteryNumbers?.winningDate ?: nextLotteryWinningDate
                    LotteryGetContent(
                        round = round,
                        winningDate = winningDate,
                        winningLotteryNumbers = winningLotteryNumbers,
                        lotteryGetNumbers =
                            lotteries.filter { it.round == round }.map { lottery ->
                                val (correctNumbers, checkWinningBonus) =
                                    winningLotteryNumbers?.toCorrectNumbers(lottery) ?: Pair(
                                        null,
                                        false,
                                    )

                                winningLotteryNumbers?.bonusNum
                                val rank =
                                    when (correctNumbers?.count { it }) {
                                        6 -> "FIRST"
                                        5 -> {
                                            if (checkWinningBonus) {
                                                "SECOND"
                                            } else {
                                                "THIRD"
                                            }
                                        }

                                        4 -> "FOURTH"
                                        3 -> "FIFTH"
                                        2 -> "SIXTH"
                                        1 -> "SEVENTH"
                                        0 -> "NONE"
                                        else -> "미발표"
                                    }

                                LotteryGetNumbers(
                                    id = lottery.id,
                                    firstNum = lottery.firstNum,
                                    secondNum = lottery.secondNum,
                                    thirdNum = lottery.thirdNum,
                                    fourthNum = lottery.fourthNum,
                                    fifthNum = lottery.fifthNum,
                                    sixthNum = lottery.sixthNum,
                                    correctNumbers = correctNumbers,
                                    rank = rank,
                                )
                            },
                    )
                }
            }

        override suspend fun getLotteryRound(): Result<Int> = lotteryDataSource.getLotteryRound()

        override suspend fun getLotteryGet(
            page: Int,
            size: Int,
        ): Result<LotteryGet> =
            lotteryDataSource
                .getLotteryGet(
                    page = page,
                    size = size,
                ).mapCatching { it.toDomain() }

        override suspend fun postLotterySave(
            firstNum: Int,
            secondNum: Int,
            thirdNum: Int,
            fourthNum: Int,
            fifthNum: Int,
            sixthNum: Int,
        ): Result<Unit> =
            lotteryDataSource.postLotterySave(
                firstNum = firstNum,
                secondNum = secondNum,
                thirdNum = thirdNum,
                fourthNum = fourthNum,
                fifthNum = fifthNum,
                sixthNum = sixthNum,
            )

        override suspend fun getLotteryRandom(): Result<LotteryRandomNumbers> =
            runCatching {
                val round =
                    nextLotteryRound ?: run {
                        getLotteryRound()
                            .map { it + 1 }
                            .getOrElse { throw it }
                    }

                val randomNumbers = generateNumbers()
                LotteryRandomNumbers(
                    round = round,
                    winningDate = nextLotteryWinningDate,
                    firstNum = randomNumbers[0],
                    secondNum = randomNumbers[1],
                    thirdNum = randomNumbers[2],
                    fourthNum = randomNumbers[3],
                    fifthNum = randomNumbers[4],
                    sixthNum = randomNumbers[5],
                )
            }

        private fun generateNumbers(): List<Int> = (1..45).shuffled().take(6)

        override suspend fun getLottoNumber(drwNo: Int): Result<LotteryNumbers> {
            lottery[drwNo]?.let {
                return Result.success(it)
            }

            val lotteryNumbers =
                lotteryDataSource.getLottoNumber(drwNo = drwNo).mapCatching { it.toDomain() }

            if (lotteryNumbers.isSuccess) {
                lottery[drwNo] = lotteryNumbers.getOrThrow()
            }

            return lotteryNumbers
        }

        private fun addDaysToDate(dateStr: String?): String {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA)
            val calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"))

            if (dateStr != null) {
                calendar.time = dateFormat.parse(dateStr)
                calendar.add(Calendar.DATE, 7)
            } else {
                val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
                val daysUntilSaturday = (Calendar.SATURDAY - dayOfWeek + 7) % 7
                calendar.add(Calendar.DAY_OF_YEAR, daysUntilSaturday)
            }

            return dateFormat.format(calendar.time)
        }
    }
