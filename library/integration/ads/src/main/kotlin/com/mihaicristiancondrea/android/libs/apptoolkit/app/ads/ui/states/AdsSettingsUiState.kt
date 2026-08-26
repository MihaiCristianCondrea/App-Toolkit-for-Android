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
 * UI model for [AdsSettingsScreen].
 *
 * Ads enablement is deliberately absent. It survives underneath as the host-facing gate, but no
 * screen writes it and nothing here reads it: reducing ads leaves ordinary ads on, so every control
 * on this screen is live regardless of it.
 *
 * @property reduceAds the user's opt-in to the host's reduced ad policy.
 */
data class AdsSettingsUiState(
    val reduceAds: Boolean = false,
)