package com.junjange.presentation.ui.editprofile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.junjange.presentation.base.BaseActivity
import com.junjange.presentation.theme.LottoTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditProfileActivity : BaseActivity() {
    private val viewModel: EditProfileViewModel by viewModels()

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
