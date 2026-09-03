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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.settings.diagnostics.ui.states

import androidx.compose.runtime.Immutable

/**
 * Represents the state for [com.mihaicristiancondrea.android.libs.apptoolkit.feature.settings.diagnostics.ui.UsageAndDiagnosticsViewModel].
 *
 * @param usageAndDiagnostics whether usage and diagnostics collection is enabled
 * @param analyticsConsent user consent for analytics
 * @param adStorageConsent user consent for ad storage
 * @param adUserDataConsent user consent for ad user data
 * @param adPersonalizationConsent user consent for ad personalization
 */
@Immutable
data class UsageAndDiagnosticsUiState(
    val usageAndDiagnostics: Boolean = false,
    val analyticsConsent: Boolean = false,
    val adStorageConsent: Boolean = false,
    val adUserDataConsent: Boolean = false,
    val adPersonalizationConsent: Boolean = false,
)
