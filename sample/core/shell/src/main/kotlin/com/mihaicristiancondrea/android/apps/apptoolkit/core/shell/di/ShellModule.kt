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

package com.mihaicristiancondrea.android.apps.apptoolkit.core.shell.di

import com.mihaicristiancondrea.android.apps.apptoolkit.core.shell.data.repositories.ComponentsShowcaseRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.core.shell.data.repositories.DefaultNavigationConfigurationRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.core.shell.data.repositories.NavigationConfigurationRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.core.shell.ui.ComponentsUnlockViewModel
import com.mihaicristiancondrea.android.apps.apptoolkit.core.shell.ui.MainViewModel
import com.mihaicristiancondrea.android.apps.apptoolkit.core.shell.ui.navigation.AppNavigationItemsProvider
import com.mihaicristiancondrea.android.apps.apptoolkit.core.shell.ui.navigation.NavigationItemsProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.app.review.domain.usecases.RequestInAppReviewUseCase
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val shellModule: Module = module {
    single<NavigationConfigurationRepository> {
        DefaultNavigationConfigurationRepository(dataStore = get())
    }
    single<NavigationItemsProvider> {
        AppNavigationItemsProvider(repository = get(), firebaseController = get())
    }
    single {
        ComponentsShowcaseRepository(dataStore = get(), firebaseController = get())
    }
    viewModel {
        MainViewModel(
            navigationItemsProvider = get(),
            consentRepository = get(),
            requestInAppReviewUseCase = get<RequestInAppReviewUseCase>(),
            inAppUpdateRepository = get(),
            firebaseController = get(),
            dispatchers = get(),
        )
    }
    viewModel {
        ComponentsUnlockViewModel(
            componentsShowcaseRepository = get(),
            dispatchers = get(),
            firebaseController = get(),
        )
    }
}
