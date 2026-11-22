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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.diagnostics.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.ScreenHost
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.ScreenStateHandler
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.shell.contentPadding
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.diagnostics.ui.analytics.USAGE_DIAGNOSTICS_SCREEN
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.diagnostics.ui.contracts.UsageAndDiagnosticsEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.diagnostics.ui.states.UsageAndDiagnosticsUiState
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.diagnostics.ui.views.dialogs.FirebaseConsentDialog
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.diagnostics.ui.views.lists.UsageAndDiagnosticsList
import org.koin.compose.viewmodel.koinViewModel

/**
 * Usage and diagnostics settings: the reporting switch, and a row that opens
 * [FirebaseConsentDialog], the same privacy choices onboarding shows. The layout matches the ads
 * settings screen, which is also one switch over the consent surface it belongs to.
 *
 * Owns [UsageAndDiagnosticsViewModel], tracking and its messages, such as a failed write.
 */
@Composable
fun UsageAndDiagnosticsScreen() {
    val viewModel: UsageAndDiagnosticsViewModel = koinViewModel()
    val state: UsageAndDiagnosticsUiState by viewModel.state.collectAsStateWithLifecycle()

    ScreenHost(screen = USAGE_DIAGNOSTICS_SCREEN, status = state.settings, viewModel = viewModel) {
        UsageAndDiagnosticsScreenContent(
            state = state,
            onEvent = viewModel::onEvent,
            contentPadding = contentPadding(),
        )
    }
}

/**
 * The reporting switch, the privacy choices row and the privacy note for [state], or its loading
 * or failure state. The privacy choices dialog's visibility is saved here, so it survives
 * rotation.
 *
 * @param onEvent Receives the events [UsageAndDiagnosticsViewModel] handles.
 * @param contentPadding Padding from the shell, applied inside the list and the state screens.
 */
@Composable
internal fun UsageAndDiagnosticsScreenContent(
    state: UsageAndDiagnosticsUiState,
    onEvent: (UsageAndDiagnosticsEvent) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
) {
    var privacyChoicesVisible: Boolean by rememberSaveable { mutableStateOf(value = false) }

    ScreenStateHandler(
        state = state.settings,
        modifier = modifier,
        contentPadding = contentPadding,
        onRetry = { onEvent(UsageAndDiagnosticsEvent.Load) },
    ) { (settings) ->
        UsageAndDiagnosticsList(
            settings = settings,
            onEvent = onEvent,
            onOpenPrivacyChoices = { privacyChoicesVisible = true },
            contentPadding = contentPadding,
        )

        if (privacyChoicesVisible) {
            FirebaseConsentDialog(
                settings = settings,
                onDismissRequest = { privacyChoicesVisible = false },
                onAllowAll = {
                    onEvent(UsageAndDiagnosticsEvent.AllowAllConsent)
                    privacyChoicesVisible = false
                },
                onAllowEssentials = {
                    onEvent(UsageAndDiagnosticsEvent.AllowEssentialConsent)
                    privacyChoicesVisible = false
                },
                onConfirmSelection = { privacyChoicesVisible = false },
                onAnalyticsConsentChanged = { granted ->
                    onEvent(UsageAndDiagnosticsEvent.SetAnalyticsConsent(granted = granted))
                },
                onAdStorageConsentChanged = { granted ->
                    onEvent(UsageAndDiagnosticsEvent.SetAdStorageConsent(granted = granted))
                },
                onAdUserDataConsentChanged = { granted ->
                    onEvent(UsageAndDiagnosticsEvent.SetAdUserDataConsent(granted = granted))
                },
                onAdPersonalizationConsentChanged = { granted ->
                    onEvent(UsageAndDiagnosticsEvent.SetAdPersonalizationConsent(granted = granted))
                },
            )
        }
    }
}
