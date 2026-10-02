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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.advanced.di

import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.settings.SettingsSearchProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.advanced.ui.search.advancedSettingsSearch
import org.koin.core.qualifier.named
import kotlinx.coroutines.flow.map
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.repositories.SeasonalThemeRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.TelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.advanced.data.repositories.CacheRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.advanced.data.repositories.DefaultCacheRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.advanced.ui.AdvancedSettingsViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val advancedSettingsModule: Module = module {
    // What the settings search finds on this page; the settings list collects every page's.
    single<SettingsSearchProvider>(named("advanced")) { advancedSettingsSearch }
    single<CacheRepository> {
        DefaultCacheRepository(
            context = get(),
            telemetryRepository = get<TelemetryRepository>(),
            dispatchers = get(),
        )
    }

    viewModel {
        AdvancedSettingsViewModel(
            repository = get(),
            dispatchers = get(),
            telemetryRepository = get(),
            // The About screen's version easter egg unlocks the developer options.
            developerOptionsUnlocked = get<SeasonalThemeRepository>().state.map { it.unlocked },
        )
    }
}
