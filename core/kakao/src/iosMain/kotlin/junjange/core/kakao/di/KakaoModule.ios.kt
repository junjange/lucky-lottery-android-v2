package junjange.core.kakao.di

import junjange.core.data.datasource.KakaoLoginDataSource
import junjange.core.kakao.datasource.IosKakaoLoginDataSource
import org.koin.core.module.Module
import org.koin.dsl.module

actual val kakaoDataSourceModule: Module = module {
    single<KakaoLoginDataSource> { IosKakaoLoginDataSource(getOrNull()) }
}
