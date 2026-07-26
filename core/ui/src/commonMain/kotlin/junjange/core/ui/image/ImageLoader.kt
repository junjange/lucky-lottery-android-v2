package junjange.core.ui.image

import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import coil3.network.ktor3.KtorNetworkFetcherFactory
import coil3.request.crossfade

/**
 * Installs a multiplatform Coil [ImageLoader] backed by the Ktor network fetcher.
 * Call once at app startup (before any `AsyncImage`). Safe to call repeatedly.
 */
fun configureImageLoader() {
    SingletonImageLoader.setSafe { context: PlatformContext ->
        ImageLoader.Builder(context)
            .components { add(KtorNetworkFetcherFactory()) }
            .crossfade(true)
            .build()
    }
}
