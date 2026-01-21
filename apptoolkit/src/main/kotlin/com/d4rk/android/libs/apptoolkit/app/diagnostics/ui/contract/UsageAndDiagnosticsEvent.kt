package com.d4rk.android.libs.apptoolkit.app.diagnostics.ui.contract

import com.d4rk.android.libs.apptoolkit.core.ui.base.handling.UiEvent

/**
 * Sealed interface representing the UI events for the Usage and Diagnostics screen.
 *
 * These events capture user interactions related to enabling or disabling usage reporting
 * and managing granular consent for analytics and advertising preferences.
 */
sealed interface UsageAndDiagnosticsEvent : UiEvent {
    data class SetUsageAndDiagnostics(val enabled: Boolean) : UsageAndDiagnosticsEvent
    data class SetAnalyticsConsent(val granted: Boolean) : UsageAndDiagnosticsEvent
    data class SetAdStorageConsent(val granted: Boolean) : UsageAndDiagnosticsEvent
    data class SetAdUserDataConsent(val granted: Boolean) : UsageAndDiagnosticsEvent
    data class SetAdPersonalizationConsent(val granted: Boolean) : UsageAndDiagnosticsEvent
}
