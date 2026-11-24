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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.diagnostics.ui.features.onboarding

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.Loadable
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.diagnostics.data.models.UsageAndDiagnosticsSettings
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.diagnostics.ui.states.UsageAndDiagnosticsUiState

@Preview(showBackground = true)
@Composable
private fun TelemetryOnboardingPageContentPreview() {
    MaterialTheme {
        TelemetryOnboardingPageContent(
            state = UsageAndDiagnosticsUiState(
                settings = Loadable.Ready(
                    value = UsageAndDiagnosticsSettings(
                        usageAndDiagnostics = true,
                        analyticsConsent = true,
                        adStorageConsent = true,
                        adUserDataConsent = true,
                        adPersonalizationConsent = true,
                    ),
                ),
            ),
            isSelected = false,
            onEvent = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TelemetryOnboardingPageContentLoadingPreview() {
    MaterialTheme {
        TelemetryOnboardingPageContent(
            state = UsageAndDiagnosticsUiState(settings = Loadable.Loading),
            isSelected = false,
            onEvent = {},
        )
    }
}
