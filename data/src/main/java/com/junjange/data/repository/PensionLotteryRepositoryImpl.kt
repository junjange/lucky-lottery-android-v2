package com.junjange.data.repository

import com.junjange.data.datasource.LotteryDataSource
import com.junjange.data.datasource.PensionLotteryDataSource
import com.junjange.data.datasource.PensionLotteryRoomDataSource
import com.junjange.data.mapper.toBonusCorrectNumbers
import com.junjange.data.mapper.toCorrectNumbers
import com.junjange.data.mapper.toDomain
import com.junjange.data.mapper.toWinningPensionLotteryBonusNumbers
import com.junjange.data.mapper.toWinningPensionLotteryNumbers
import com.junjange.data.model.local.PensionLotteryNumberDto
import com.junjange.domain.model.PensionLotteryGet
import com.junjange.domain.model.PensionLotteryGetContent
import com.junjange.domain.model.PensionLotteryHome
import com.junjange.domain.model.PensionLotteryNumbers
import com.junjange.domain.model.PensionLotteryRandom
import com.junjange.domain.repository.PensionLotteryRepository
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject

internal class PensionLotteryRepositoryImpl
    @Inject
    constructor(
        private val lotteryDataSource: LotteryDataSource,
        private val pensionLotteryDataSource: PensionLotteryDataSource,
        private val pensionLotteryRoomDataSource: PensionLotteryRoomDataSource,
    ) : PensionLotteryRepository {
        private val pensionLottery: LinkedHashMap<Int, PensionLotteryHome> = linkedMapOf()

        private val nextRound
            get() = pensionLottery.keys.first() + 1

        private val nextWinningDate
            get() = addDaysToDate(pensionLottery.values.first().winningDate)

        override suspend fun postPensionLotterySave(
            pensionGroup: Int,
            pensionFirstNum: Int,
            pensionSecondNum: Int,
            pensionThirdNum: Int,
            pensionFourthNum: Int,
            pensionFifthNum: Int,
            pensionSixthNum: Int,
        ): Result<Unit> =
            pensionLotteryDataSource.postPensionLotterySave(
                pensionGroup = pensionGroup,
                pensionFirstNum = pensionFirstNum,
                pensionSecondNum = pensionSecondNum,
                pensionThirdNum = pensionThirdNum,
                pensionFourthNum = pensionFourthNum,
                pensionFifthNum = pensionFifthNum,
                pensionSixthNum = pensionSixthNum,
            )

        override suspend fun getPensionLotteryRandom(): Result<PensionLotteryRandom> =
            runCatching {
                val randomNumbers = generateNumbers()
                val group = generateGroupNumber()
                PensionLotteryRandom(
                    pensionRound = nextRound,
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

        override suspend fun getPensionLotteryGet(
            page: Int,
            size: Int,
        ): Result<PensionLotteryGet> =
            pensionLotteryDataSource
                .getPensionLotteryGet(
                    page = page,
                    size = size,
                ).mapCatching { it.toDomain() }

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
                    .getOrDefault(emptyList())

            if (pagedRounds.isEmpty()) return Result.failure(Exception("No rounds available"))

            val lotteries =
                pensionLotteryRoomDataSource
                    .getPensionLotteriesByRound(pagedRounds)
                    .getOrDefault(emptyList())

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
            val pensionLotteryNumberDto =
                PensionLotteryNumberDto(
                    round = nextRound,
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
            runCatching {
                pensionLotteryRoomDataSource.deletePensionLotteryByRoundAndId(
                    round = round,
                    id = id,
                )
            }

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

        private fun addDaysToDate(dateStr: String): String {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA)

            val calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"))
            calendar.time = dateFormat.parse(dateStr)!!

            calendar.add(Calendar.DATE, 7)

            return dateFormat.format(calendar.time)
        }
    }
