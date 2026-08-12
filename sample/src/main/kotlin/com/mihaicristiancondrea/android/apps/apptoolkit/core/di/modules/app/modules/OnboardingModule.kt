/*
 * Copyright (©) 2026 Mihai-Cristian Condrea
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package com.mihaicristiancondrea.android.apps.apptoolkit.core.di.modules.app.modules

import com.mihaicristiancondrea.android.apps.apptoolkit.app.onboarding.utils.interfaces.providers.AppOnboardingProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.app.onboarding.data.repository.OnboardingRepositoryImpl
import com.mihaicristiancondrea.android.libs.apptoolkit.app.onboarding.domain.repository.OnboardingRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.app.onboarding.domain.usecases.CompleteOnboardingUseCase
import com.mihaicristiancondrea.android.libs.apptoolkit.app.onboarding.domain.usecases.ObserveOnboardingCompletionUseCase
import com.mihaicristiancondrea.android.libs.apptoolkit.app.onboarding.ui.OnboardingViewModel
import com.mihaicristiancondrea.android.libs.apptoolkit.app.onboarding.utils.interfaces.providers.OnboardingProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.core.data.local.datastore.CommonDataStore
import com.mihaicristiancondrea.android.libs.apptoolkit.core.data.local.datastore.interfaces.OnboardingPreferencesDataSource
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val onboardingModule: Module = module {
    single<OnboardingProvider> { AppOnboardingProvider() }
    single<OnboardingPreferencesDataSource> { get<CommonDataStore>() }
    single<OnboardingRepository> { OnboardingRepositoryImpl(dataStore = get()) }
    single<ObserveOnboardingCompletionUseCase> { ObserveOnboardingCompletionUseCase(repository = get()) }
    single<CompleteOnboardingUseCase> { CompleteOnboardingUseCase(repository = get()) }

    viewModel {
        OnboardingViewModel(
            observeOnboardingCompletionUseCase = get(),
            completeOnboardingUseCase = get(),
            dispatchers = get(),
            firebaseController = get(),
        )
    }
}

