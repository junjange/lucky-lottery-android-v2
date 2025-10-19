package com.junjange.lotto3.di

import com.junjange.lotto3.navigation.MainNavigatorImpl
import com.junjange.lotto3.navigation.RandomNumberGenerationNavigatorImpl
import com.junjange.lotto3.navigation.RandomNumberNavigatorImpl
import com.junjange.lotto3.navigation.RegisterNavigatorImpl
import com.junjange.lotto3.navigation.SplashNavigatorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import junjange.core.navigation.MainNavigator
import junjange.core.navigation.RandomNumberGenerationNavigator
import junjange.core.navigation.RandomNumberNavigator
import junjange.core.navigation.RegisterNavigator
import junjange.core.navigation.SplashNavigator
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class NavigatorModule {
    @Binds
    @Singleton
    abstract fun bindMainNavigator(impl: MainNavigatorImpl): MainNavigator

    @Binds
    @Singleton
    abstract fun bindRandomNumberNavigator(impl: RandomNumberNavigatorImpl): RandomNumberNavigator

    @Binds
    @Singleton
    abstract fun bindRandomNumberGenerationNavigator(impl: RandomNumberGenerationNavigatorImpl): RandomNumberGenerationNavigator

    @Binds
    @Singleton
    abstract fun bindSplashNavigator(impl: SplashNavigatorImpl): SplashNavigator

    @Binds
    @Singleton
    abstract fun bindRegisterNavigator(impl: RegisterNavigatorImpl): RegisterNavigator
}
