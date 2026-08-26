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

import com.mihaicristiancondrea.android.apps.apptoolkit.core.utils.constants.ads.AdsConstants
import kotlinx.coroutines.flow.StateFlow

/**
 * The sample's answer to "how intrusive should ads be right now?".
 *
 * The toolkit's ads integration knows only whether it can initialize and render an ad; deciding
 * whether this app *wants* one is host policy, so it lives here rather than in `AdsCoreManager` or
 * in the generic native-ad composables. One governor keeps every surface interpreting the reduced
 * policy the same way instead of each feature inventing its own numbers.
 *
 * The legacy `adsEnabled` gate is upstream of all of this: when it is off nothing renders at all,
 * and [appOpenAdsEnabled] already accounts for it.
 */
interface SampleAdsPolicy {

    /** The user's reduced-ads opt-in. */
    val reduceAds: StateFlow<Boolean>

    /**
     * Whether an App Open ad may be shown when the process comes to the foreground.
     *
     * `false` both for grandfathered ad-free installs and under the reduced policy — dropping the
     * App Open ad is the largest part of what "reduce ads" promises.
     */
    val appOpenAdsEnabled: StateFlow<Boolean>

    /**
     * How many content items to show between consecutive native ads at [placement].
     *
     * Reads [reduceAds] at call time, so callers that render the result must key their
     * `remember` on the collected preference value.
     */
    fun nativeAdInterval(placement: NativeAdPlacement): Int
}

/**
 * A surface of the sample that interleaves native ads into a list.
 *
 * @property normalInterval items between ads under the normal policy.
 */
enum class NativeAdPlacement(val normalInterval: Int) {
    APPS(normalInterval = AdsConstants.APPS_LIST_AD_FREQUENCY),
    QUICK_TOOLS(normalInterval = AdsConstants.QUICK_TOOLS_AD_FREQUENCY),
}
