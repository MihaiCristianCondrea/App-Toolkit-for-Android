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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.di

import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.data.providers.GooglePlayServicesVersionProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.data.repositories.AboutRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.data.repositories.DefaultAboutRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.ui.AboutViewModel
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.ui.factory.GmsHostFactory
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.data.repositories.DefaultNavigationRepository
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val aboutModule: Module = module {
    single { GooglePlayServicesVersionProvider(context = get()) }

    single<DefaultAboutRepository> {
        DefaultAboutRepository(
            deviceProvider = get(),
            buildInfoProvider = get(),
            firebaseController = get(),
            gmsVersionProvider = get(),
        )
    }
    single<AboutRepository> { get<DefaultAboutRepository>() }

    single<DefaultNavigationRepository> {
        DefaultNavigationRepository(
            dispatchers = get(),
        )
    }

    viewModel {
        AboutViewModel(
            aboutRepository = get(),
            context = get(),
            dispatchers = get(),
            firebaseController = get(),
        )
    }

    single { GmsHostFactory() }
}
