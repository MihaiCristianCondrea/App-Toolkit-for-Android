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

package com.d4rk.android.libs.apptoolkit.core.ui.views.ads

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.d4rk.android.libs.apptoolkit.core.data.local.datastore.CommonDataStore
import com.d4rk.android.libs.apptoolkit.core.data.local.datastore.rememberCommonDataStore

/**
 * A Composable function that remembers and observes whether ads are enabled.
 *
 * This function retrieves the ads enabled status from [CommonDataStore] and collects it as a
 * state that recomposes the view when the value changes. It defaults to `true` if no value is set.
 *
 * @return `true` if ads are enabled, `false` otherwise. The value is lifecycle-aware.
 */
@Composable
fun rememberAdsEnabled(): Boolean {
    val dataStore: CommonDataStore = rememberCommonDataStore()
    return remember { dataStore.ads(default = true) }
        .collectAsStateWithLifecycle(initialValue = true).value
}
