package junjange.core.firebase.di

import com.google.firebase.messaging.FirebaseMessaging
import org.koin.dsl.module

val firebaseModule = module {
    single { FirebaseMessaging.getInstance() }
}
