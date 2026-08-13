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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.ads

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mihaicristiancondrea.android.libs.apptoolkit.core.data.local.datastore.CommonDataStore
import com.mihaicristiancondrea.android.libs.apptoolkit.core.data.local.datastore.rememberCommonDataStore

/**
 * A Composable function that remembers and observes whether ads are enabled.
 *
 * Change rationale: this used to build its own flow with a hardcoded `default = true`, while
 * [AdsCoreManager] gated SDK initialization on the same preference read with a *different* default.
 * When the two disagreed, ad views loaded ads for an SDK that had never been initialized, and the
 * loader throws for that. Both sides now read [CommonDataStore.adsEnabledFlow], which carries the
 * default the host configured, so they cannot diverge.
 *
 * @return `true` if ads are enabled, `false` otherwise. The value is lifecycle-aware.
 */
@Composable
fun rememberAdsEnabled(): Boolean {
    val dataStore: CommonDataStore = rememberCommonDataStore()
    return dataStore.adsEnabledFlow.collectAsStateWithLifecycle().value
}
