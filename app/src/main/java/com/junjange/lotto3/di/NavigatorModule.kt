package com.junjange.lotto3.di

import com.junjange.lotto3.navigation.MainNavigatorImpl
import com.junjange.lotto3.navigation.RandomNumberGenerationNavigatorImpl
import com.junjange.lotto3.navigation.RandomNumberNavigatorImpl
import com.junjange.lotto3.navigation.SplashNavigatorImpl
import junjange.core.navigation.MainNavigator
import junjange.core.navigation.RandomNumberGenerationNavigator
import junjange.core.navigation.RandomNumberNavigator
import junjange.core.navigation.SplashNavigator
import org.koin.dsl.module

val navigatorModule = module {
    single<MainNavigator> { MainNavigatorImpl() }
    single<RandomNumberNavigator> { RandomNumberNavigatorImpl() }
    single<RandomNumberGenerationNavigator> { RandomNumberGenerationNavigatorImpl() }
    single<SplashNavigator> { SplashNavigatorImpl() }
}
