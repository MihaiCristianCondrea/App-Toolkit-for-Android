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

package com.mihaicristiancondrea.android.libs.apptoolkit.app.ads.ui.states

/**
 * How the ads screen labels its single toggle.
 *
 * The toggle writes one preference either way; only its wording and what the build does with it
 * differ. Carried in UI state rather than read from `BuildConfig` in the composable so the screen
 * stays a function of its state and both labels are reachable from a test.
 */
enum class AdsToggleMode {
    /** Release builds: the opt-in stops app-open ads and leaves the rest. */
    REDUCE,

    /** Debug builds: the opt-in stops every ad, so ad-free behaviour can be checked. */
    DISABLE,
}

/**
 * UI model for [AdsSettingsScreen].
 *
 * @property limitAds the user's opt-in, and the only value the switch writes.
 * @property mode which wording the toggle carries in this build.
 */
data class AdsSettingsUiState(
    val limitAds: Boolean = false,
    val mode: AdsToggleMode = AdsToggleMode.REDUCE,
) {

    /**
     * Whether the personalized-ads row has anything to act on.
     *
     * Personalization shapes the ads that get shown, so the row is live except in the one state
     * where none are: a debug build with [AdsToggleMode.DISABLE] switched on. A release build keeps
     * showing ads under the opt-in, so the row stays live there.
     *
     * `AdsDisplayPolicy` is what actually stops the ads; this restates the condition so the row and
     * the switch move together during the optimistic update instead of the row lagging a write.
     * The two must stay in step — see the module README.
     */
    val personalizedAdsEnabled: Boolean
        get() = !(mode == AdsToggleMode.DISABLE && limitAds)
}
