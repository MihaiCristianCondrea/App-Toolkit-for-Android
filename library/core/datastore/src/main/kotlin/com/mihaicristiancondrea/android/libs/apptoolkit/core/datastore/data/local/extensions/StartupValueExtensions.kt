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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.local.extensions

import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.local.CommonDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

/**
 * Projects the stored startup route without depending on a navigation framework.
 *
 * Blank routes use [defaultRoute]; callers decide how to handle unknown or legacy routes.
 * Consecutive equal mapped values are emitted only once.
 */
fun <T> CommonDataStore.startupValueFlow(
    defaultRoute: String,
    mapToValue: (String) -> T,
): Flow<T> = getStartupPage(default = defaultRoute)
    .map { route -> mapToValue(route.ifBlank { defaultRoute }) }
    .distinctUntilChanged()
