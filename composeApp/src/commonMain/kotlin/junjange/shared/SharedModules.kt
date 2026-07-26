package junjange.shared

import junjange.core.data.di.repositoryModule
import junjange.core.domain.di.useCaseModule
import junjange.core.remote.di.remoteDataSourceModule
import junjange.core.remote.di.remoteModule
import org.koin.core.module.Module

val sharedModules: List<Module> = listOf(
    remoteModule,
    remoteDataSourceModule,
    repositoryModule,
    useCaseModule,
)
