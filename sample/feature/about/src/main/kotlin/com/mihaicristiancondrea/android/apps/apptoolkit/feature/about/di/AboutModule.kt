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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.about.di

import com.mihaicristiancondrea.android.apps.apptoolkit.feature.about.ui.providers.AppAboutSettingsProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.di.models.AppToolkitHostBuildConfig
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.ui.providers.AboutSettingsProvider
import org.koin.core.module.Module
import org.koin.dsl.module

fun aboutModule(hostBuildConfig: AppToolkitHostBuildConfig): Module = module {
    single<AboutSettingsProvider> {
        AppAboutSettingsProvider(context = get(), hostBuildConfig = hostBuildConfig)
    }
}