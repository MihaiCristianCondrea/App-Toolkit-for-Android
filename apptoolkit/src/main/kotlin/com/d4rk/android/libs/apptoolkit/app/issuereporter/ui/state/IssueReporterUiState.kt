package com.d4rk.android.libs.apptoolkit.app.issuereporter.ui.state

/**
 * UI state holder for the Issue Reporter screen.
 */
data class IssueReporterUiState(
    val title: String = "",
    val description: String = "",
    val email: String = "",
    val anonymous: Boolean = true,
    val issueUrl: String? = null
)