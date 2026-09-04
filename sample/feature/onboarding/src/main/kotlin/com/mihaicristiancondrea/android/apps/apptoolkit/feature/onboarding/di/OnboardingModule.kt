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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.onboarding.di

import com.mihaicristiancondrea.android.apps.apptoolkit.feature.onboarding.ui.AppOnboardingProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.local.CommonDataStore
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.local.interfaces.OnboardingPreferencesDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.onboarding.data.repositories.DefaultOnboardingRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.onboarding.data.repositories.OnboardingRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.onboarding.ui.OnboardingViewModel
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.onboarding.ui.providers.OnboardingProvider
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val onboardingModule: Module = module {
    single<OnboardingProvider> { AppOnboardingProvider() }
    single<OnboardingPreferencesDataSource> { get<CommonDataStore>() }
    single<OnboardingRepository> { DefaultOnboardingRepository(dataStore = get()) }

    viewModel {
        OnboardingViewModel(
            onboardingRepository = get(),
            dispatchers = get(),
            firebaseController = get(),
        )
    }
}
