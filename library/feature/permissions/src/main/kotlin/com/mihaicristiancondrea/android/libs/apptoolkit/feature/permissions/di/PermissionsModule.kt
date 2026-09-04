package com.mihaicristiancondrea.android.libs.apptoolkit.feature.permissions.di

import com.mihaicristiancondrea.android.libs.apptoolkit.feature.permissions.data.repositories.DefaultPermissionsRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.permissions.data.repositories.PermissionsRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.permissions.ui.PermissionsViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val permissionsModule: Module = module {
    single<PermissionsRepository> {
        DefaultPermissionsRepository(
            context = get(),
            dispatchers = get(),
            firebaseController = get(),
        )
    }

    viewModel {
        PermissionsViewModel(
            permissionsRepository = get(),
            dispatchers = get(),
            firebaseController = get(),
        )
    }
}
