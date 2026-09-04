package com.mihaicristiancondrea.android.libs.apptoolkit.feature.support.di

import com.mihaicristiancondrea.android.libs.apptoolkit.feature.support.ui.SupportViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val supportModule: Module = module {
    viewModel {
        SupportViewModel(billingRepository = get(), firebaseController = get())
    }
}
