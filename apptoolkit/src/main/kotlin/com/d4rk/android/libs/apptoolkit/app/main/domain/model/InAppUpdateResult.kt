package com.d4rk.android.libs.apptoolkit.app.main.domain.model

/**
 * Result of an in-app update request.
 */
sealed interface InAppUpdateResult {
    /** The update flow started successfully. */
    data object Started : InAppUpdateResult

    /** No update is available for the current install. */
    data object NotAvailable : InAppUpdateResult

    /** An update is available but immediate updates are not permitted. */
    data object NotAllowed : InAppUpdateResult

    /** The update flow failed to start. */
    data object Failed : InAppUpdateResult
}
