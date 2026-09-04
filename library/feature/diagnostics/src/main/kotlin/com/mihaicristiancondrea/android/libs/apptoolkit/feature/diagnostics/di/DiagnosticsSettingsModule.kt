package com.mihaicristiancondrea.android.libs.apptoolkit.feature.diagnostics.di

import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.local.CommonDataStore
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.diagnostics.data.repositories.DefaultUsageAndDiagnosticsRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.diagnostics.data.repositories.UsageAndDiagnosticsRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.diagnostics.ui.UsageAndDiagnosticsViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val diagnosticsSettingsModule: Module = module {
    single<UsageAndDiagnosticsRepository> {
        DefaultUsageAndDiagnosticsRepository(
            dataSource = get<CommonDataStore>(),
            configProvider = get(),
            dispatchers = get(),
            firebaseController = get(),
        )
    }

    viewModel {
        UsageAndDiagnosticsViewModel(
            repository = get(),
            firebaseController = get(),
            dispatchers = get(),
            consentRepository = get(),
        )
    }
}
