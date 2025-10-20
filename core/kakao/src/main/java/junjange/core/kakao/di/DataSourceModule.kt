package junjange.core.kakao.di

import junjange.core.data.datasource.KakaoLoginDataSource
import junjange.core.kakao.datasource.KakaoLoginDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class DataSourceModule {
    @Binds
    abstract fun bindsKakaoLoginDataSource(kakaoLoginDataSourceImpl: KakaoLoginDataSourceImpl): KakaoLoginDataSource
}
