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

import kotlinx.coroutines.flow.StateFlow

/**
 * The sample's answer to "do I want an App Open ad right now?".
 *
 * The toolkit's ads integration knows only whether it can initialize and show an ad; whether this
 * app wants one is host policy, so it lives here rather than in `AdsCoreManager`.
 *
 * This is the half of the ads toggle that does *not* vary by build: the opt-in stops app-open ads
 * in every build. Whether the remaining native and banner slots keep rendering is
 * `AdsDisplayPolicy`'s decision, and that one does vary.
 */
interface SampleAdsPolicy {

    /** Whether an App Open ad may be shown when the process comes to the foreground. */
    val appOpenAdsEnabled: StateFlow<Boolean>
}
