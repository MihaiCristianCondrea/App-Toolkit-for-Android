package com.mihaicristiancondrea.android.libs.apptoolkit.feature.advanced.di

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.FirebaseController
import com.mihaicristiancondrea.android.libs.apptoolkit.core.network.domain.models.network.DataState
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.advanced.data.repositories.CacheRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.advanced.data.repositories.DefaultCacheRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.advanced.ui.AdvancedSettingsViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val advancedSettingsModule: Module = module {
    single<CacheRepository> {
        DefaultCacheRepository(
            context = get(),
            firebaseController = get<FirebaseController>(),
        )
    }

    viewModel {
        AdvancedSettingsViewModel(
            repository = get(),
            dispatchers = get(),
            firebaseController = get(),
        )
    }
}
