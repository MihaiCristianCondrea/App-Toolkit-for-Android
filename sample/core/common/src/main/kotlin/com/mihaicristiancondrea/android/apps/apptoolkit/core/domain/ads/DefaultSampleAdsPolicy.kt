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

package com.mihaicristiancondrea.android.apps.apptoolkit.core.domain.ads

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn

/**
 * Derives the sample's ad policy from the two persisted preferences.
 *
 * Takes flows rather than the preference store so this stays a pure policy object: it is the only
 * place that knows the reduced policy means "no App Open ads, native ads at twice the spacing".
 *
 * Both values are shared eagerly because [appOpenAdsEnabled] is read synchronously from a process
 * lifecycle callback, which cannot wait for a first emission. Until the preferences arrive both
 * read `false`, so the app under-shows ads for a moment rather than showing one the user opted out
 * of.
 *
 * @param adsEnabled the legacy hard gate.
 * @param reduceAdsPreference the user's reduced-ads opt-in.
 * @param scope scope backing the shared state; it must live as long as the process.
 */
class DefaultSampleAdsPolicy(
    adsEnabled: Flow<Boolean>,
    reduceAdsPreference: Flow<Boolean>,
    scope: CoroutineScope,
) : SampleAdsPolicy {

    override val reduceAds: StateFlow<Boolean> = reduceAdsPreference
        .distinctUntilChanged()
        .stateIn(scope = scope, started = SharingStarted.Eagerly, initialValue = false)

    override val appOpenAdsEnabled: StateFlow<Boolean> =
        combine(adsEnabled, reduceAdsPreference) { enabled, reduced -> enabled && !reduced }
            .distinctUntilChanged()
            .stateIn(scope = scope, started = SharingStarted.Eagerly, initialValue = false)

    override fun nativeAdInterval(placement: NativeAdPlacement): Int =
        if (reduceAds.value) placement.normalInterval * REDUCED_INTERVAL_FACTOR
        else placement.normalInterval

    private companion object {
        /**
         * Reduced spacing is derived from the normal cadence rather than being a second set of
         * tuning numbers, so retuning a placement keeps both policies in step.
         */
        const val REDUCED_INTERVAL_FACTOR: Int = 2
    }
}
