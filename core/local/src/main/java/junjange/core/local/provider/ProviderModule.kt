package junjange.core.local.provider

import junjange.core.data.provider.AccessTokenProvider
import junjange.core.data.provider.RefreshTokenProvider
import org.koin.dsl.module

val providerModule = module {
    single<AccessTokenProvider> { AccessTokenProviderImpl(get()) }
    single<RefreshTokenProvider> { RefreshTokenProviderImpl(get()) }
}
