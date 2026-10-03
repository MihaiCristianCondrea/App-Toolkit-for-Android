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

import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.settings.SettingsSearchProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.ui.search.aboutSettingsSearch
import org.koin.core.qualifier.named
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.data.providers.GooglePlayServicesVersionProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.data.repositories.AboutRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.data.repositories.DefaultAboutRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.ui.AboutViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val aboutModule: Module = module {
    single<SettingsSearchProvider>(named("about")) { aboutSettingsSearch }
    single { GooglePlayServicesVersionProvider(context = get()) }

    single<DefaultAboutRepository> {
        DefaultAboutRepository(
            deviceProvider = get(),
            buildInfoProvider = get(),
            telemetryRepository = get(),
            gmsVersionProvider = get(),
            dispatchers = get(),
        )
    }
    single<AboutRepository> { get<DefaultAboutRepository>() }

    viewModel {
        AboutViewModel(
            aboutRepository = get(),
            clipboardRepository = get(),
            telemetryRepository = get(),
            seasonalThemes = get(),
        )
    }
}
