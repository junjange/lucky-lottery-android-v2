package junjange.feature.editprofile.di

import junjange.feature.editprofile.EditProfileViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val editProfileViewModelModule = module {
    viewModel {
        EditProfileViewModel(
            savedStateHandle = get(),
            patchUserProfileUseCase = get(),
            postImagesUploadUseCase = get()
        )
    }
}
