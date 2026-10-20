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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry

import androidx.compose.runtime.Stable

/**
 * The switches that decide what the telemetry SDKs may collect, set from the person's consent.
 *
 * Only the code that owns consent and the usage and diagnostics choice applies them, so the SDKs
 * collect exactly what the person agreed to. It reports nothing itself.
 */
@Stable
interface TelemetryCollection {

    /** Updates the consent configuration used by analytics and ads measurement. */
    fun updateConsent(
        analyticsGranted: Boolean,
        adStorageGranted: Boolean,
        adUserDataGranted: Boolean,
        adPersonalizationGranted: Boolean,
    )

    /** Enables or disables analytics collection for the current process. */
    fun setAnalyticsEnabled(enabled: Boolean)

    /** Enables or disables crash reporting for the current process. */
    fun setCrashlyticsEnabled(enabled: Boolean)

    /** Enables or disables performance monitoring for the current process. */
    fun setPerformanceEnabled(enabled: Boolean)
}
