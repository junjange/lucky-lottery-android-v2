package junjange.feature.withdrawal

import org.jetbrains.compose.resources.StringResource
import junjange.feature.withdrawal.resources.*
import junjange.feature.withdrawal.resources.withdrawal_answer_01
import junjange.feature.withdrawal.resources.withdrawal_answer_02
import junjange.feature.withdrawal.resources.withdrawal_answer_03
import junjange.feature.withdrawal.resources.withdrawal_answer_04
import junjange.feature.withdrawal.resources.withdrawal_answer_05
import junjange.feature.withdrawal.resources.withdrawal_answer_06

enum class WithdrawalOption(
    val textRes: StringResource,
) {
    Option1(textRes = Res.string.withdrawal_answer_01),
    Option2(textRes = Res.string.withdrawal_answer_02),
    Option3(textRes = Res.string.withdrawal_answer_03),
    Option4(textRes = Res.string.withdrawal_answer_04),
    Option5(textRes = Res.string.withdrawal_answer_05),
    Option6(textRes = Res.string.withdrawal_answer_06),
}
