package junjange.core.kakao.di

import junjange.core.data.datasource.KakaoLoginDataSource
import junjange.core.kakao.datasource.KakaoLoginDataSourceImpl
import org.koin.dsl.module

val kakaoDataSourceModule = module {
    single<KakaoLoginDataSource> { KakaoLoginDataSourceImpl(get()) }
}
