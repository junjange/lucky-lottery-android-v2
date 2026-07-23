package junjange.core.kakao.di

import junjange.core.data.datasource.KakaoLoginDataSource
import junjange.core.kakao.datasource.KakaoLoginDataSourceImpl
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

actual val kakaoDataSourceModule: Module = module {
    single<KakaoLoginDataSource> { KakaoLoginDataSourceImpl(androidContext()) }
}
