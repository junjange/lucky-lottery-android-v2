package junjange.shared

import org.koin.core.context.startKoin

fun initKoin() {
    startKoin {
        modules(sharedModules)
    }
}
