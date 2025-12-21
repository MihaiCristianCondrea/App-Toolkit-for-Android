package com.d4rk.android.libs.apptoolkit.app.diagnostics.ui.contract

import com.d4rk.android.libs.apptoolkit.core.ui.base.handling.UiEvent

sealed interface UsageAndDiagnosticsEvent : UiEvent {
    data class SetUsageAndDiagnostics(val enabled: Boolean) : UsageAndDiagnosticsEvent
    data class SetAnalyticsConsent(val granted: Boolean) : UsageAndDiagnosticsEvent
    data class SetAdStorageConsent(val granted: Boolean) : UsageAndDiagnosticsEvent
    data class SetAdUserDataConsent(val granted: Boolean) : UsageAndDiagnosticsEvent
    data class SetAdPersonalizationConsent(val granted: Boolean) : UsageAndDiagnosticsEvent
}
