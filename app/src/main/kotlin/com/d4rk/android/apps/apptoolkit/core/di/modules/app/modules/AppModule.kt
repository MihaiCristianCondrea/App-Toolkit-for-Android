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

package com.d4rk.android.apps.apptoolkit.core.di.modules.app.modules

import com.d4rk.android.apps.apptoolkit.app.main.data.repository.MainNavigationRepositoryImpl
import com.d4rk.android.apps.apptoolkit.app.main.domain.usecases.GetNavigationDrawerItemsUseCase
import com.d4rk.android.apps.apptoolkit.app.main.ui.MainViewModel
import com.d4rk.android.apps.apptoolkit.app.main.ui.navigation.NavigationManager
import com.d4rk.android.apps.apptoolkit.app.tiles.data.repository.ToolkitTilesRepositoryImpl
import com.d4rk.android.apps.apptoolkit.app.tiles.domain.repository.ToolkitTilesRepository
import com.d4rk.android.apps.apptoolkit.app.tiles.domain.usecase.GetToolkitTilesUseCase
import com.d4rk.android.apps.apptoolkit.app.tiles.domain.usecase.SyncToolkitTileStatusesUseCase
import com.d4rk.android.apps.apptoolkit.app.tiles.ui.ToolkitTilesViewModel
import com.d4rk.android.libs.apptoolkit.app.main.domain.repository.NavigationRepository
import com.d4rk.android.libs.apptoolkit.app.review.domain.usecases.RequestInAppReviewUseCase
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule: Module = module {
    single { NavigationManager() }
    single<NavigationRepository> { MainNavigationRepositoryImpl(dataStore = get(), firebaseController = get()) }
    single<GetNavigationDrawerItemsUseCase> {
        GetNavigationDrawerItemsUseCase(navigationRepository = get(), firebaseController = get())
    }
    single { GetToolkitTilesUseCase() }
    single { SyncToolkitTileStatusesUseCase(repository = get()) }
    single<ToolkitTilesRepository> { ToolkitTilesRepositoryImpl(context = androidContext()) }
    viewModel {
        ToolkitTilesViewModel(
            getToolkitTilesUseCase = get(),
            syncToolkitTileStatusesUseCase = get(),
            dispatchers = get(),
            firebaseController = get(),
        )
    }

    viewModel {
        MainViewModel(
            getNavigationDrawerItemsUseCase = get(),
            applyInitialConsentUseCase = get(),
            requestConsentUseCase = get(),
            requestInAppReviewUseCase = get<RequestInAppReviewUseCase>(),
            requestInAppUpdateUseCase = get(),
            firebaseController = get(),
            dispatchers = get(),
        )
    }
}
