package com.d4rk.android.libs.apptoolkit.app.issuereporter.ui.contract

import com.d4rk.android.libs.apptoolkit.core.ui.base.handling.UiEvent

sealed interface IssueReporterEvent : UiEvent {
    data class UpdateTitle(val value: String) : IssueReporterEvent
    data class UpdateDescription(val value: String) : IssueReporterEvent
    data class UpdateEmail(val value: String) : IssueReporterEvent
    data class SetAnonymous(val anonymous: Boolean) : IssueReporterEvent
    data object Send : IssueReporterEvent
    data object DismissSnackbar : IssueReporterEvent
}