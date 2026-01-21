package junjange.feature.editprofile

import android.graphics.Bitmap
import androidx.lifecycle.SavedStateHandle
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import junjange.core.domain.usecase.ImagesUploadUseCase
import junjange.core.domain.usecase.PatchUserProfileUseCase
import junjange.core.ui.base.BaseViewModel
import junjange.feature.editprofile.EditProfileEffect.LaunchImagePicker
import junjange.feature.editprofile.EditProfileEffect.ProfileUpdateSuccess
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.io.File

class EditProfileViewModel
    constructor(
        private val savedStateHandle: SavedStateHandle,
        private val patchUserProfileUseCase: PatchUserProfileUseCase,
        private val postImagesUploadUseCase: ImagesUploadUseCase,
    ) : BaseViewModel() {
        private val _uiState = MutableStateFlow(EditProfileState())
        val uiState: StateFlow<EditProfileState> = _uiState.asStateFlow()

        private val _effect = MutableSharedFlow<EditProfileEffect>()
        val effect: SharedFlow<EditProfileEffect> = _effect.asSharedFlow()

        init {
            initUserInfo()
        }

        private fun initUserInfo() {
            val nickname =
                requireNotNull(
                    savedStateHandle.get<String>(EditProfileActivity.EXTRA_KEY_NICKNAME),
                )

            val profileImage = savedStateHandle.get<String?>(EditProfileActivity.EXTRA_KEY_PROFILE_PATH)

            _uiState.update {
                it.copy(
                    currentNickName = nickname,
                    newNickname = nickname,
                    currentProfileImage = profileImage,
                    newProfileImage = profileImage,
                )
            }
        }

        fun postImagesUpload() {
            launch {
                _uiState.value.profileImageFile?.let { file ->
                    val multipartContent =
                        MultiPartFormDataContent(
                            formData {
                                append(
                                    "file",
                                    file.readBytes(),
                                    Headers.build {
                                        append(HttpHeaders.ContentType, "image/*")
                                        append(HttpHeaders.ContentDisposition, "filename=\"${file.name}\"")
                                    },
                                )
                            },
                        )
                    postImagesUploadUseCase(multipartContent)
                        .onSuccess { imageUpload ->
                            patchUserProfile(imageUpload.imageUrl)
                        }.onFailure {
                            // TODO 예외처리
                        }
                } ?: run {
                    patchUserProfile(uiState.value.newProfileImage)
                }
            }
        }

        private fun patchUserProfile(profilePath: String?) {
            launch {
                patchUserProfileUseCase(
                    profilePath = profilePath,
                    nickname = uiState.value.newNickname,
                ).onSuccess {
                    launch {
                        _effect.emit(ProfileUpdateSuccess)
                    }
                }.onFailure {
                    // TODO 예외처리
                }
            }
        }

        fun setImageFile(file: File) {
            _uiState.update {
                it.copy(profileImageFile = file)
            }
        }

        fun onPickImage(bitmap: Bitmap) {
            _uiState.update {
                it.copy(
                    isBottomSheetShowing = false,
                    newProfileImageBitmap = bitmap,
                    newProfileImage = bitmap.toString(),
                )
            }
        }

        fun inputNickname(value: String) {
            _uiState.update { it.copy(newNickname = value) }
        }

        fun setBottomSheetShowing(isBottomSheetShowing: Boolean) {
            _uiState.update { it.copy(isBottomSheetShowing = isBottomSheetShowing) }
        }

        fun onClickedProfileImgSelect() {
            launch {
                _effect.emit(LaunchImagePicker)
            }
        }

        fun onClickedProfileDefaultImageSelect() {
            _uiState.update {
                it.copy(
                    profileImageFile = null,
                    newProfileImage = null,
                    isBottomSheetShowing = false,
                    newProfileImageBitmap = null,
                )
            }
        }
    }
