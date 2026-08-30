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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.components.di

import com.mihaicristiancondrea.android.apps.apptoolkit.app.components.data.repositories.ComponentsShowcaseRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.app.components.ui.navigation.ComponentsNavigationContribution
import com.mihaicristiancondrea.android.apps.apptoolkit.core.navigation.NavigationItemContribution
import org.koin.dsl.bind
import org.koin.dsl.module

val componentsFeatureModule = module {
    single {
        ComponentsShowcaseRepository(
            dataStore = get(),
            firebaseController = get(),
        )
    }
    single { ComponentsNavigationContribution(repository = get()) } bind NavigationItemContribution::class
}
