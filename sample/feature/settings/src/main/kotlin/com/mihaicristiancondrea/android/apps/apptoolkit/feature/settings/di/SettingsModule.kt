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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.settings.di

import com.mihaicristiancondrea.android.apps.apptoolkit.feature.settings.data.repositories.ShowcaseUnlockRepository
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * The sample's own settings additions: the About page's hidden unlock of the components showcase.
 *
 * The toolkit's settings extension points are answered in `:sample:core:apptoolkit`, with the rest
 * of the App Toolkit setup.
 */
val settingsModule: Module = module {
    single {
        ShowcaseUnlockRepository(
            dataStore = get(),
            telemetryRepository = get(),
        )
    }
}
