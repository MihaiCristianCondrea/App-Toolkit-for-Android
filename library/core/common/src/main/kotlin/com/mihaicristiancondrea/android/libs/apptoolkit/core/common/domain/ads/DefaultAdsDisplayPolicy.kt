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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.ads

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.CoroutineScope

/**
 * Answers [adsAllowed] from the limit-ads opt-in and the build type.
 *
 * Takes a `Flow` rather than the preference store so this stays a pure policy object, and shares
 * eagerly because ad slots read the value during composition.
 *
 * A release build never consults the preference at all — it is a constant `true`, with no collector
 * and no scope work — which is the property that keeps the reduced policy from quietly becoming an
 * ad-free build for everyone.
 *
 * @param limitAds the user's limit-ads opt-in.
 * @param isDebugBuild the host's own build type, taken from one source so nothing can disagree.
 * @param scope scope backing the shared state; it must live as long as the process.
 */
class DefaultAdsDisplayPolicy(
    limitAds: Flow<Boolean>,
    isDebugBuild: Boolean,
    scope: CoroutineScope,
) : AdsDisplayPolicy {

    override val adsAllowed: StateFlow<Boolean> =
        if (isDebugBuild) {
            limitAds
                .map { limited -> !limited }
                .distinctUntilChanged()
                // Ads are allowed until the preference arrives, so an early slot behaves the way a
                // release build always does rather than blinking empty on every launch.
                .stateIn(scope = scope, started = SharingStarted.Eagerly, initialValue = true)
        } else {
            MutableStateFlow(value = true)
        }
}
