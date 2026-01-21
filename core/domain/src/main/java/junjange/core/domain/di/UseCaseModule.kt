package junjange.core.domain.di

import junjange.core.domain.usecase.*
import org.koin.dsl.module

val useCaseModule = module {
    // Auth & Token UseCases
    factory { GetJwtTokenUseCase(get()) }
    factory { SaveJwtTokenUseCase(get()) }
    factory { GetIdTokenUseCase(get()) }
    factory { SaveIdTokenUseCase(get()) }
    factory { GetFCMTokenUseCase(get()) }

    // Login & Auth UseCases
    factory { KakaoLoginUseCase(get()) }
    factory { PostLoginUseCase(get()) }
    factory { PostLogoutUseCase(get()) }
    factory { GetValidRegisterUseCase(get()) }
    factory { PostRegisterUseCase(get()) }
    factory { PostGoogleOauthTokenUseCase(get()) }

    // User UseCases
    factory { GetUserMyInfoUseCase(get()) }
    factory { PatchUserProfileUseCase(get()) }
    factory { DeleteMeUseCase(get()) }
    factory { DeleteLocalDataUseCase(get()) }

    // Lottery UseCases
    factory { GetLotteryUseCase(get()) }
    factory { GetLotteryRoundUseCase(get()) }
    factory { GetLotteryHomeUseCase(get()) }
    factory { GetLotteryRandomUseCase(get()) }
    factory { GetLotteryGetUseCase(get()) }
    factory { PostLotterySaveUseCase(get()) }
    factory { InsertLotteryUseCase(get()) }
    factory { LoadLotteryRoundsUseCase(get()) }
    factory { DeleteLotteryByRoundAndIdUseCase(get()) }

    // Pension Lottery UseCases
    factory { GetPensionLotteryUseCase(get()) }
    factory { GetPensionLotteryRoundUseCase(get()) }
    factory { GetPensionLotteryHomeUseCase(get()) }
    factory { GetPensionLotteryRandomUseCase(get()) }
    factory { GetPensionLotteryGetUseCase(get()) }
    factory { PostPensionLotterySaveUseCase(get()) }
    factory { InsertPensionLotteryUseCase(get()) }
    factory { LoadPensionLotteryRoundsUseCase(get()) }
    factory { DeletePensionLotteryByRoundAndIdUseCase(get()) }

    // Notification UseCases
    factory { GetNotificationUseCase(get()) }
    factory { PatchLotteryNotificationUseCase(get()) }
    factory { PatchPensionLotteryNotificationUseCase(get()) }
    factory { PostNotificationRegisterTokenUseCase(get()) }

    // Images UseCase
    factory { ImagesUploadUseCase(get()) }
}
