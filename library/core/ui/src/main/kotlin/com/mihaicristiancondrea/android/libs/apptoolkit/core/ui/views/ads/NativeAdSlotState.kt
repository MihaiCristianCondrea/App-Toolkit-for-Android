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

import androidx.compose.runtime.Immutable
import com.google.android.libraries.ads.mobile.sdk.nativead.NativeAd

/**
 * What a slot has: an ad, or the reason it has none.
 *
 * [rememberNativeAd] returns only the ad, because that is all a caller needs to render one. A
 * caller that wants to say something about an empty slot, such as the debug placeholder, needs to
 * know whether the request came back without an ad or was never made at all, which is what this
 * carries.
 *
 * @property ad the loaded ad, or null while loading, after a failure, or when disabled.
 * @property failure why there is no ad, or null when one is loaded or still on its way.
 * @property detail the SDK's own description of the failure, when it gave one.
 */
@Immutable
data class NativeAdSlotState(
    val ad: NativeAd? = null,
    val failure: AdSlotFailure? = null,
    val detail: String? = null,
)
