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
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.ads.AdsDisplayPolicy
import org.koin.compose.koinInject

/**
 * Whether this ad slot may request and render an ad.
 *
 * Always `true` in a release build; in a debug build it follows the ads screen's toggle, which
 * reads "Disable ads" there. See [AdsDisplayPolicy] for why the two builds differ.
 *
 * Every slot resolves the same process-scoped [AdsDisplayPolicy] and collects one already-shared
 * `StateFlow`, so no slot can pick a default of its own. That is the whole point: a slot that
 * decided for itself whether ads were on is how the SDK once ended up uninitialized while the views
 * loaded ads anyway, which took the host process down.
 *
 * @return `true` when the slot may load. The value is lifecycle-aware.
 */
@Composable
fun rememberAdsAllowed(): Boolean {
    val policy: AdsDisplayPolicy = koinInject()
    val adsAllowed: Boolean by policy.adsAllowed.collectAsStateWithLifecycle()
    return adsAllowed
}
