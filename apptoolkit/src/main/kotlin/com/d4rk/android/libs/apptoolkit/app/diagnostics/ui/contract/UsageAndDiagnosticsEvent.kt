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

package com.d4rk.android.libs.apptoolkit.app.diagnostics.ui.contract

import com.d4rk.android.libs.apptoolkit.core.ui.base.handling.UiEvent

/**
 * Sealed interface representing the UI events for the Usage and Diagnostics screen.
 *
 * These events capture user interactions related to enabling or disabling usage reporting
 * and managing granular consent for analytics and advertising preferences.
 */
sealed interface UsageAndDiagnosticsEvent : UiEvent {
    data object Initialize : UsageAndDiagnosticsEvent
    data class SetUsageAndDiagnostics(val enabled: Boolean) : UsageAndDiagnosticsEvent
    data class SetAnalyticsConsent(val granted: Boolean) : UsageAndDiagnosticsEvent
    data class SetAdStorageConsent(val granted: Boolean) : UsageAndDiagnosticsEvent
    data class SetAdUserDataConsent(val granted: Boolean) : UsageAndDiagnosticsEvent
    data class SetAdPersonalizationConsent(val granted: Boolean) : UsageAndDiagnosticsEvent
}
