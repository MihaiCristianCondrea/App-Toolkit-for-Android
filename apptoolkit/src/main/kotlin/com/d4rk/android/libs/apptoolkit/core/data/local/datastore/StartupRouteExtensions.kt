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

package com.d4rk.android.libs.apptoolkit.core.data.local.datastore

import com.d4rk.android.libs.apptoolkit.core.ui.model.navigation.StableNavKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Maps the stored startup page to a stable navigation key.
 *
 * The mapping function allows apps to convert persisted string routes into their
 * own navigation key implementations while keeping the lookup reusable.
 */
fun <T : StableNavKey> CommonDataStore.startupDestinationFlow(
    defaultRoute: String,
    mapToKey: (String) -> T,
): Flow<T> = getStartupPage(default = defaultRoute).map { route ->
    val safeRoute = route.ifBlank { defaultRoute }
    mapToKey(safeRoute)
}
