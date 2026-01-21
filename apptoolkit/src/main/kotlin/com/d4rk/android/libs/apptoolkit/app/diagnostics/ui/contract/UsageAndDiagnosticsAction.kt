package com.d4rk.android.libs.apptoolkit.app.diagnostics.ui.contract

import com.d4rk.android.libs.apptoolkit.core.ui.base.handling.ActionEvent

/**
 * Represents the set of possible user-triggered actions and intents
 * within the Usage and Diagnostics UI component.
 *
 * This interface is used to communicate intent from the UI to the
 * ViewModel or Controller, adhering to the MVI/Unidirectional Data Flow pattern.
 */
sealed interface UsageAndDiagnosticsAction : ActionEvent
