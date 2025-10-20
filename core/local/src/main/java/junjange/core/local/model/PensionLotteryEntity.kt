package junjange.core.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import junjange.core.data.model.local.PensionLotteryNumberDto
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "pension_lottery")
data class PensionLotteryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "round")
    val round: Int,
    @ColumnInfo(name = "group")
    val group: Int,
    @ColumnInfo(name = "firstNum")
    val firstNum: Int,
    @ColumnInfo(name = "secondNum")
    val secondNum: Int,
    @ColumnInfo(name = "thirdNum")
    val thirdNum: Int,
    @ColumnInfo(name = "fourthNum")
    val fourthNum: Int,
    @ColumnInfo(name = "fifthNum")
    val fifthNum: Int,
    @ColumnInfo(name = "sixthNum")
    val sixthNum: Int,
)

fun PensionLotteryNumberDto.toLocal() =
    PensionLotteryEntity(
        round = round,
        group = group,
        firstNum = firstNum,
        secondNum = secondNum,
        thirdNum = thirdNum,
        fourthNum = fourthNum,
        fifthNum = fifthNum,
        sixthNum = sixthNum,
    )

fun List<PensionLotteryEntity>.toData() = map { it.toData() }

fun PensionLotteryEntity.toData() =
    PensionLotteryNumberDto(
        id = id,
        round = round,
        group = group,
        firstNum = firstNum,
        secondNum = secondNum,
        thirdNum = thirdNum,
        fourthNum = fourthNum,
        fifthNum = fifthNum,
        sixthNum = sixthNum,
    )
