package com.junjange.data.repository

import com.junjange.data.datasource.LotteryDataSource
import com.junjange.data.datasource.LotteryRoomDataSource
import com.junjange.data.mapper.toCorrectNumbers
import com.junjange.data.mapper.toDomain
import com.junjange.data.mapper.toWinningLotteryNumbers
import com.junjange.data.model.local.LotteryNumberDto
import com.junjange.domain.model.LotteryGet
import com.junjange.domain.model.LotteryGetContent
import com.junjange.domain.model.LotteryGetNumbers
import com.junjange.domain.model.LotteryNumbers
import com.junjange.domain.model.LotteryRandomNumbers
import com.junjange.domain.repository.LotteryRepository
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
            get() = lottery.keys.first() + 1

        private val nextLotteryWinningDate
            get() = addDaysToDate(lottery.values.first().winningDate)

        override suspend fun loadLotteryRounds(
            page: Int,
            size: Int,
        ): Result<List<LotteryGetContent>> {
            val pagedRounds =
                lotteryRoomDataSource
                    .getPagedRounds(limit = size, offset = page * size)
                    .getOrDefault(emptyList())

            if (pagedRounds.isEmpty()) return Result.failure(Exception("No rounds available"))

            val lotteries =
                lotteryRoomDataSource.getLotteriesByRound(pagedRounds).getOrDefault(emptyList())

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
            val lotteryNumberDto =
                LotteryNumberDto(
                    round = nextLotteryRound,
                    firstNum = firstNum,
                    secondNum = secondNum,
                    thirdNum = thirdNum,
                    fourthNum = fourthNum,
                    fifthNum = fifthNum,
                    sixthNum = sixthNum,
                )
            return lotteryRoomDataSource.insertLottery(lotteryNumberDto = lotteryNumberDto)
        }

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
                                        0 -> "꽝"
                                        else -> "미발표"
                                    }

                                LotteryGetNumbers(
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
                val randomNumbers = generateNumbers()
                LotteryRandomNumbers(
                    round = nextLotteryRound,
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

        private fun addDaysToDate(dateStr: String): String {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA)

            val calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"))
            calendar.time = dateFormat.parse(dateStr)!!

            calendar.add(Calendar.DATE, 7)

            return dateFormat.format(calendar.time)
        }
    }
