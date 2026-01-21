package junjange.feature.notification.di

import junjange.feature.notification.NotificationViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val notificationViewModelModule = module {
    viewModel {
        NotificationViewModel(
            getNotificationUseCase = get(),
            patchLotteryNotificationUseCase = get(),
            patchPensionLotteryNotificationUseCase = get()
        )
    }
}
