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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.privacy.di

import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.settings.SettingsSearchProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.privacy.ui.search.privacySettingsSearch
import org.koin.core.qualifier.named
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.privacy.ui.PrivacyViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val privacyModule: Module = module {
    // What the settings search finds on this page; the settings list collects every page's.
    single<SettingsSearchProvider>(named("privacy")) { privacySettingsSearch }
    viewModel {
        PrivacyViewModel(
            provider = get(),
            telemetryRepository = get(),
        )
    }
}
