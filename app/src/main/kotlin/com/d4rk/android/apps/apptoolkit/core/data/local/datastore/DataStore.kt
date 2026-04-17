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

package com.d4rk.android.apps.apptoolkit.core.data.local.datastore

import android.content.Context
import com.d4rk.android.apps.apptoolkit.BuildConfig
import com.d4rk.android.libs.apptoolkit.core.coroutines.dispatchers.DispatcherProvider
import com.d4rk.android.libs.apptoolkit.core.coroutines.dispatchers.StandardDispatchers
import com.d4rk.android.libs.apptoolkit.core.data.local.datastore.CommonDataStore

/**
 * Host app datastore implementation.
 *
 * This class intentionally adds no extra behavior yet; it exists to provide a stable app-level
 * type that can evolve independently while delegating persistence behavior to [CommonDataStore].
 */
class DataStore(
    context: Context,
    dispatchers: DispatcherProvider = StandardDispatchers(),
    defaultAdsEnabled: Boolean = !BuildConfig.DEBUG,
) : CommonDataStore(
    context = context,
    dispatchers = dispatchers,
    defaultAdsEnabled = defaultAdsEnabled,
), DatastoreInterface
