package junjange.feature.editprofile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import org.koin.androidx.viewmodel.ext.android.viewModel
// import androidx.activity.viewModels
import junjange.core.designsystem.theme.LottoTheme
import junjange.core.ui.base.BaseActivity


class EditProfileActivity : BaseActivity() {
    private val viewModel: EditProfileViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LottoTheme {
                EditProfileScreen(
                    viewModel = viewModel,
                    onBack = ::finish,
                )
            }
        }
    }

    companion object {
        const val EXTRA_KEY_NICKNAME = "EXTRA_KEY_NICKNAME"
        const val EXTRA_KEY_PROFILE_PATH = "EXTRA_KEY_PROFILE_PATH"

        fun startActivity(
            context: Context,
            nickname: String,
            profilePath: String?,
        ) {
            val intent =
                Intent(context, EditProfileActivity::class.java)
                    .putExtra(EXTRA_KEY_NICKNAME, nickname)
                    .putExtra(EXTRA_KEY_PROFILE_PATH, profilePath)
            context.startActivity(intent)
        }
    }
}
