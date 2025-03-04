package com.junjange.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.junjange.data.model.local.LotteryNumberDto
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "lottery")
data class LotteryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "round")
    val round: Int,
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

fun LotteryNumberDto.toLocal() =
    LotteryEntity(
        round = round,
        firstNum = firstNum,
        secondNum = secondNum,
        thirdNum = thirdNum,
        fourthNum = fourthNum,
        fifthNum = fifthNum,
        sixthNum = sixthNum,
    )

fun List<LotteryEntity>.toData() = map { it.toData() }

fun LotteryEntity.toData() =
    LotteryNumberDto(
        round = round,
        id = id,
        firstNum = firstNum,
        secondNum = secondNum,
        thirdNum = thirdNum,
        fourthNum = fourthNum,
        fifthNum = fifthNum,
        sixthNum = sixthNum,
    )
