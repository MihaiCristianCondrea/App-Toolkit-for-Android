package com.mihaicristiancondrea.android.libs.apptoolkit.integration.update.di

import com.mihaicristiancondrea.android.libs.apptoolkit.integration.update.data.repositories.DefaultInAppUpdateRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.update.data.repositories.InAppUpdateRepository
import org.koin.core.module.Module
import org.koin.dsl.module

/** Binds the Play in-app update repository for consuming features. */
fun updateModule(): Module = module {
    single<InAppUpdateRepository> { DefaultInAppUpdateRepository() }
}
