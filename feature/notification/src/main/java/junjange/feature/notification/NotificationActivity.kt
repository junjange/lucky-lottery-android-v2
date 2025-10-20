package junjange.feature.notification

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import junjange.core.designsystem.theme.LottoTheme
import junjange.core.ui.base.BaseActivity

@AndroidEntryPoint
class NotificationActivity : BaseActivity() {
    private val viewModel: NotificationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LottoTheme {
                NotificationScreen(viewModel = viewModel)
            }
        }
    }

    companion object {
        const val EXTRA_KEY_LOTTO_NOTIFICATION_STATE = "EXTRA_KEY_LOTTO_NOTIFICATION_STATE"
        const val EXTRA_KEY_PENSION_LOTTO_NOTIFICATION_STATE =
            "EXTRA_KEY_PENSION_LOTTO_NOTIFICATION_STATE"

        fun startActivity(
            context: Context,
            lottoNotificationState: Boolean,
            pensionLottoNotificationState: Boolean,
        ) {
            val intent =
                Intent(context, NotificationActivity::class.java)
                    .putExtra(EXTRA_KEY_LOTTO_NOTIFICATION_STATE, lottoNotificationState)
                    .putExtra(EXTRA_KEY_PENSION_LOTTO_NOTIFICATION_STATE, pensionLottoNotificationState)
            context.startActivity(intent)
        }
    }
}
