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

package com.d4rk.android.apps.apptoolkit.app.tiles.ui.views.ads

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.d4rk.android.libs.apptoolkit.core.ui.views.ads.NativeAdPresentation
import com.d4rk.android.libs.apptoolkit.core.ui.views.ads.NativeAdSlot
import com.d4rk.android.libs.apptoolkit.core.ui.views.preferences.GroupedItemPosition

/**
 * Native ad row rendered between Toolkit Tiles categories.
 *
 * Change rationale: this used to inflate `R.layout.native_ad_quick_tools_row` through
 * `NativeAdViewHost` and bind it with `findViewById`. It is now a thin wrapper over the toolkit's
 * shared [NativeAdSlot], so the row picks up the same lifecycle, palette and disclosure handling as
 * every other ad surface.
 *
 * The Toolkit Tiles list is an exception to the toolkit's default of drawing ads on a plain card:
 * its rows are built as `surfaceContainerLow` surfaces, so the ad passes that container explicitly
 * rather than inheriting a tint the rest of the app does not want.
 *
 * @param initiallyLoaded keeps an already reported ad visible while this composable is moved from
 * the hidden preloader slot into the visible list. The slot reports `false` before its own load
 * resolves; suppressing only that first report avoids a transient status reset and layout gap.
 */
@Composable
fun QuickToolsNativeAdCard(
    modifier: Modifier = Modifier,
    adUnitId: String,
    position: GroupedItemPosition,
    initiallyLoaded: Boolean = false,
    onStatusChanged: (Boolean) -> Unit = {},
) {
    var isFirstReport: Boolean by remember(adUnitId) { mutableStateOf(value = true) }

    NativeAdSlot(
        adUnitId = adUnitId,
        presentation = NativeAdPresentation.Compact,
        modifier = modifier.fillMaxWidth(),
        position = position,
        containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        onAdLoaded = { isLoaded ->
            val suppressTransientReset: Boolean = isFirstReport && initiallyLoaded && !isLoaded
            isFirstReport = false
            if (!suppressTransientReset) {
                onStatusChanged(isLoaded)
            }
        },
    )
}
